
## JWT 私钥公钥生成

- 生成 JKS 文件

```bash
keytool -genkeypair -alias myalias -storetype PKCS12 -keyalg RSA -keypass keypass -keystore mykeystore.jks -storepass storepass -validity 3650
```

- 导出公钥

```bash
# 保存为 public.cer 文件：
keytool -exportcert -alias myalias -storepass storepass -keystore mykeystore.jks -file public.cer

# 保存为 public.key 文件
keytool -list -rfc --keystore mykeystore.jks -storepass storepass | openssl x509 -inform pem -pubkey > public.key
```

- 导出私钥，将其保存为 private.key 文件：

```bash
keytool -importkeystore -srckeystore mykeystore.jks -srcstorepass storepass -destkeystore private.p12 -deststoretype PKCS12 -deststorepass storepass -destkeypass keypass

#输入密码 storepass
openssl pkcs12 -in private.p12 -nodes -nocerts -out private.key
```

## OAuth2 测试

1. 授权请求URL：

http://127.0.0.1:9000/oauth2/authorize?client_id=messaging-client&redirect_uri=http://127.0.0.1:8081/authorized&response_type=code&scope=message.write

2. 授权码获取Token

```bash
CODE=1ZBsaDyI-08LckcY9b4mAKn8Gxr0EfJKkG2uePcHP2Eou9BVxFBBt4cOKNhw9BMh7hMR2B0uRw9raUWnDKLUNDbKumYmHvgfCHGKd2_FE5nWGjg3Rp4KLsDLOwk_UNny

RESPONSE=$(curl --request POST -s --url http://messaging-client:secret@127.0.0.1:9000/oauth2/token \
    --header 'content-type: multipart/form-data' \
    --form redirect_uri=http://127.0.0.1:8081/authorized \
    --form grant_type=authorization_code \
    --form code=$CODE )

echo $RESPONSE

ACCESS_TOKEN=$(echo  $RESPONSE | jq .access_token -r)
REFRESH_TOKEN=$(echo  $RESPONSE | jq .refresh_token -r)

echo ACCESS_TOKEN=$ACCESS_TOKEN
echo REFRESH_TOKEN=$REFRESH_TOKEN
```

3. 刷新Token：

```bash
REFRESH_TOKEN=b6CG0YIDIkLPIVf-bwCcaW6gm99PdQndN6QoDs1PgCgF32BT5dwoKIDuWKGNyRG_7qq0lSSsSD0Cp4kpXnHFLCAsMJB-g-DUtMCGxWr2TGEUL6fmWPV2F9pKsuiQ7y-O

ACCESS_TOKEN=$(curl --request POST -s --url http://messaging-client:secret@127.0.0.1:9000/oauth2/token --header 'content-type: multipart/form-data' \
    --form grant_type=refresh_token --form refresh_token=$REFRESH_TOKEN  | jq .access_token -r)
echo ACCESS_TOKEN=$ACCESS_TOKEN
```

4. 访问资源

```bash
curl http://localhost:9000/?access_token=eyJraWQiOiJlYTU4NDBmNS0yMTI4LTRhNGItODY5OS02MWM0MDc1ZGQ5ZjUiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiYXVkIjoibWVzc2FnaW5nLWNsaWVudCIsIm5iZiI6MTcwOTY5MTc5Nywic2NvcGUiOlsibWVzc2FnZS53cml0ZSJdLCJpc3MiOiJodHRwOi8vMTI3LjAuMC4xOjkwMDAiLCJleHAiOjE3MDk2OTIwOTcsImlhdCI6MTcwOTY5MTc5N30.lAeZPjyg1xo-MtJ59nfSvSnJrp7wRNcrpFWo-NaD1PFD_bCVTX8no9ZifoHESq04_LJQnOAOPinHHwlU1l4ROcnoeDc1UcQgiWeTwVNebnkCmw8DW5p2aJXbudx097uk52qaScbntYdcNUcE933ANf7S0lopXPcVfn8id937wxpYsQBJBz6vGi3_OJgsbHndV9D46c6NBJ6I3ujN6dTEje8kFRC2P4R35gjxDWFJNqVQIiFngWlczMulZgMMbAC9JUGHh9oS6KgNqbU9Pu0H1IkcxDXE7VLfgHLT7A9d3-wLEC3ZOu7RzXxxbtsk8remsU47UaIFRK9KohtKowX7uQ

```
