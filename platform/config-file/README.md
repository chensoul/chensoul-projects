

curl http://user:123456@localhost:8443/config/auth-service/default -ks | jq .


curl -k http://user:123456@localhost:8443/config/encrypt --data-urlencode "hello world"
