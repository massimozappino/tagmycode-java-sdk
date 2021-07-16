[![Build Status](https://travis-ci.com/massimozappino/tagmycode-java-sdk.svg?branch=master)](https://travis-ci.com/massimozappino/tagmycode-java-sdk)
[![Coverage Status](https://coveralls.io/repos/github/massimozappino/tagmycode-java-sdk/badge.svg)](https://coveralls.io/github/massimozappino/tagmycode-java-sdk)

# TagMyCode Java SDK
A Java library to access [TagMyCode RESTful API](http://tagmycode.com).

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
