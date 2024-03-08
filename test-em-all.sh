#!/usr/bin/env bash
#
# Sample usage:
#
#   HOST=localhost PORT=8443 ./test-em-all.sh
#
: ${HOST=localhost}
: ${PORT=8443}
: ${SKIP_CB_TESTS=false}

function assertCurl() {

    local expectedHttpCode=$1
    local curlCmd="$2 -w \"%{http_code}\""
    local result=$(eval $curlCmd)
    local httpCode="${result:(-3)}"
    RESPONSE='' && ((${#result} > 3)) && RESPONSE="${result%???}"

    if [ "$httpCode" = "$expectedHttpCode" ]; then
        if [ "$httpCode" = "200" ]; then
            business_code=$(echo $RESPONSE | jq .code)
            if [ "$business_code" = "0" ] || [ "$business_code" = null ]; then
                echo "Test OK (HTTP Code: $httpCode)"
            else
                echo "Test FAILED, EXPECTED HTTP Code: $expectedHttpCode, GOT: $httpCode -> $business_code, WILL ABORT!"
                echo "- Failing command: $curlCmd"
                echo "- Response Body: $RESPONSE"
            fi
        else
            echo "Test OK (HTTP Code: $httpCode, $RESPONSE)"
        fi
    else
        echo "Test FAILED, EXPECTED HTTP Code: $expectedHttpCode, GOT: $httpCode, WILL ABORT!"
        echo "- Failing command: $curlCmd"
        echo "- Response Body: $RESPONSE"
        exit 1
    fi
}

function assertEqual() {

    local expected=$1
    local actual=$2

    if [ "$actual" = "$expected" ]; then
        echo "Test OK (actual value: $actual)"
    else
        echo "Test FAILED, EXPECTED VALUE: $expected, ACTUAL VALUE: $actual, WILL ABORT"
        exit 1
    fi
}

function testUrl() {
    url=${@}
    if $url -ks -f -o /dev/null; then
        return 0
    else
        return 1
    fi
}

function waitForService() {
    url=${@}
    echo "Wait for: $url... "
    n=0
    until testUrl $url; do
        n=$((n + 1))
        if [[ $n == 30 ]]; then
            echo " Give up"
            exit 1
        else
            sleep 3
            echo -e ", retry #$n "
        fi
    done
    echo "DONE, continues..."
}

function testCircuitBreaker() {

    echo "Start Circuit Breaker tests!"

    # First, use the health - endpoint to verify that the circuit breaker is closed
    assertEqual "CLOSED" "$(curl -s http://$HOST:$PORT/auth/actuator/health | jq -r .components.circuitBreakers.details.authservice.details.state)"

    # Open the circuit breaker by running three slow calls in a row, i.e. that cause a timeout exception
    # Also, verify that we get 500 back and a timeout related error message
    for ((n = 0; n < 10; n++)); do
        assertCurl 200 "curl http://$HOST:$PORT/auth/user/info?delay=3 $AUTH -s"
        message=$(echo $RESPONSE | jq -r .message)
        #        assertEqual "Did not observe any item or terminal signal within 2000ms" "${message:0:57}"
    done

    # Verify that the circuit breaker is open
    assertEqual "OPEN" "$(curl -s http://$HOST:$PORT/auth/actuator/health | jq -r .components.circuitBreakers.details.authservice.details.state)"

    # Verify that the circuit breaker now is open by running the slow call again, verify it gets 200 back, i.e. fail fast works, and a response from the fallback method.
    assertCurl 200 "curl http://$HOST:$PORT/auth/user/info?delay=3 $AUTH -s"
    assertEqual "Fallback get user info" "$(echo "$RESPONSE" | jq -r .name)"

    # Also, verify that the circuit breaker is open by running a normal call, verify it also gets 200 back and a response from the fallback method.
    assertCurl 200 "curl http://$HOST:$PORT/auth/user/info $AUTH -s"
    assertEqual "Fallback get user info" "$(echo "$RESPONSE" | jq -r .name)"

    # Verify that a 404 (Not Found) error is returned for a non existing productId ($PROD_ID_NOT_FOUND) from the fallback method.
    assertCurl 404 "curl http://$HOST:$PORT/auth/user/info $AUTH -s"
    assertEqual "Product Id: $PROD_ID_NOT_FOUND not found in fallback cache!" "$(echo $RESPONSE | jq -r .message)"

    # Wait for the circuit breaker to transition to the half open state (i.e. max 10 sec)
    echo "Will sleep for 10 sec waiting for the CB to go Half Open..."
    sleep 10

    # Verify that the circuit breaker is in half open state
    assertEqual "HALF_OPEN" "$(curl -s http://$HOST:$PORT/auth/actuator/health | jq -r .components.circuitBreakers.details.authservice.details.state)"

    # Close the circuit breaker by running three normal calls in a row
    # Also, verify that we get 200 back and a response based on information in the product database
    for ((n = 0; n < 3; n++)); do
        assertCurl 200 "curl http://$HOST:$PORT/auth/user/info $AUTH -s"
        assertEqual "user name C" "$(echo "$RESPONSE" | jq -r .data.username)"
    done

    # Verify that the circuit breaker is in closed state again
    assertEqual "CLOSED" "$(curl -s http://$HOST:$PORT/auth/actuator/health | jq -r .components.circuitBreakers.details.authservice.details.state)"

    # Verify that the expected state transitions happened in the circuit breaker
    assertEqual "CLOSED_TO_OPEN" "$(curl -s http://$HOST:$PORT/auth/actuator/circuitbreakerevents/authservice/STATE_TRANSITION | jq -r .circuitBreakerEvents[-3].stateTransition)"
    assertEqual "OPEN_TO_HALF_OPEN" "$(curl -s http://$HOST:$PORT/auth/actuator/circuitbreakerevents/authservice/STATE_TRANSITION | jq -r .circuitBreakerEvents[-2].stateTransition)"
    assertEqual "HALF_OPEN_TO_CLOSED" "$(curl -s http://$HOST:$PORT/auth/actuator/circuitbreakerevents/authservice/STATE_TRANSITION | jq -r .circuitBreakerEvents[-1].stateTransition)"
}

set -e

echo "Start Tests:" "$(date)"

echo "HOST=${HOST}"
echo "PORT=${PORT}"
echo "SKIP_CB_TESTS=${SKIP_CB_TESTS}"

if [[ $* == *"start"* ]]; then
    echo "Package and build docker image..."
    mvn clean package -DskipTest && docker compose build
    echo "Restarting the test environment..."
    echo "$ docker compose down --remove-orphans"
    docker compose down --remove-orphans
    echo "$ docker compose up -d"
    docker compose up -d
    docker ps --format "{{.Names}}"
fi

waitForService curl -k http://$HOST:$PORT/actuator/health

# client_credentials
ACCESS_TOKEN=$(curl -k http://client:secret@$HOST:$PORT/auth/oauth/token -d grant_type=client_credentials -d scope="api all" -s | jq .access_token -r)
echo ACCESS_TOKEN=$ACCESS_TOKEN
AUTH="-H \"Authorization: Bearer $ACCESS_TOKEN\""

# password
ACCESS_TOKEN=$(curl -k http://client:secret@$HOST:$PORT/auth/oauth/token -d grant_type=password -d scope="api all" -d username=user -d password=123456 -s | jq .access_token -r)
echo ACCESS_TOKEN=$ACCESS_TOKEN
AUTH="-H \"Authorization: Bearer $ACCESS_TOKEN\""

# Verify access to Eureka and that all microservices are registered in Eureka
assertCurl 200 "curl -H "accept:application/json" -k http://root:123456@$HOST:$PORT/eureka/api/apps -s"
assertEqual 2 $(echo $RESPONSE | jq ".applications.application | length")

# Verify access to the Config server and that its encrypt/decrypt endpoints work
assertCurl 200 "curl -H "accept:application/json" -k http://root:123456@$HOST:$PORT/config/auth-service/default -s"
TEST_VALUE="hello world"
ENCRYPTED_VALUE=$(curl -k http://root:123456@$HOST:$PORT/config/encrypt --data-urlencode "$TEST_VALUE" -s)
DECRYPTED_VALUE=$(curl -k http://root:123456@$HOST:$PORT/config/decrypt -d $ENCRYPTED_VALUE -s)
assertEqual "$TEST_VALUE" "$DECRYPTED_VALUE"

assertCurl 200 "curl $AUTH -k http://$HOST:$PORT/auth/user/info -s"
assertEqual "user" "$(echo $RESPONSE | jq -r .data.username)"

# Verify access to Swagger and OpenAPI URLs
#echo "Swagger/OpenAPI tests"
#assertCurl 302 "curl -ks  http://$HOST:$PORT/openapi/swagger-ui.html"
#assertCurl 200 "curl -ksL http://$HOST:$PORT/openapi/swagger-ui.html"
#assertCurl 200 "curl -ks  http://$HOST:$PORT/openapi/webjars/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config"
#assertCurl 200 "curl -ks  http://$HOST:$PORT/openapi/v3/api-docs"
#assertEqual "3.0.1" "$(echo $RESPONSE | jq -r .openapi)"
#assertEqual "http://$HOST:$PORT" "$(echo $RESPONSE | jq -r '.servers[0].url')"
#assertCurl 200 "curl -ks  http://$HOST:$PORT/openapi/v3/api-docs.yaml"

if [[ $SKIP_CB_TESTS == "false" ]]; then
    testCircuitBreaker
fi

if [[ $* == *"stop"* ]]; then
    echo "We are done, stopping the test environment..."
    echo "$ docker compose down"
    docker compose down
fi

echo "End, all tests OK:" $(date)
