

curl http://user:devpwd@localhost:8443/config/auth-server/default -ks | jq .


curl -k http://user:devpwd@localhost:8443/config/encrypt --data-urlencode "hello world"
