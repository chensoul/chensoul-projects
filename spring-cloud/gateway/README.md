```bash
curl 192.168.1.107:8443/actuator/gateway/routes -s | jq '.[] | {"\(.route_id)": "\(.uri)"}' | grep -v '{\|}'
```

curl -H "accept:application/json" 192.168.1.107:8443/eureka/apps -s | jq -r '.applications.application[].instance[] | .instanceId'
