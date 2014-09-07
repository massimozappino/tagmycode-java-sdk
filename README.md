# TagMyCode Java SDK
[![Build Status](https://travis-ci.org/massimozappino/tagmycode-java-sdk.svg?branch=master)](https://travis-ci.org/massimozappino/tagmycode-java-sdk)

## Examples

### Authenticating
```java
Client client = new Client("consumer_id", "consumer_secret");
client.setOauthToken(new OauthToken("access_token", "refresh_token"));
```

### Get account information for logged user
```java
TagMyCode tagmycode = new TagMyCode(client)
User user = tagmycode.getAccount();
```
