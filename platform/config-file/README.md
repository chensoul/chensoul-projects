

curl http://root:123456@localhost:8443/config/foodie-diner/default -ks | jq .


curl -k http://root:123456@localhost:8443/config/encrypt --data-urlencode "hello world"