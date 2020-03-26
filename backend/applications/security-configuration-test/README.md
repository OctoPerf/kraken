User
```
curl -s -X POST -H "Content-Type: application/x-www-form-urlencoded" -d 'username=kraken-user&password=kraken&grant_type=password' -d 'client_id=kraken-web' "http://localhost:9080/auth/realms/kraken/protocol/openid-connect/token" | jq -r '.access_token' > build/token
```

Admin
```
curl -s -X POST -H "Content-Type: application/x-www-form-urlencoded" -d 'username=kraken-admin&password=kraken&grant_type=password' -d 'client_id=kraken-web' "http://localhost:9080/auth/realms/kraken/protocol/openid-connect/token" | jq -r '.access_token' > build/token
```

List files
```
curl --verbose -X GET http://localhost:8080/test/user -H "Authorization: Bearer $(cat build/token)"
```