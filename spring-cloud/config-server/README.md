

curl http://user:dev-pwd@localhost:8443/config/auth-server/default -ks | jq .


curl -k http://user:dev-pwd@localhost:8443/config/encrypt --data-urlencode "hello world"
