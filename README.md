# Gift Certificates Advanced API

this API allows you to do create, update, update and delete aka on entities

| Entity      |  C  |  R  |  U  |  D  |
|:------------|:---:|:---:|:---:|:---:|
| Certificate |  +  |  +  |  +  |  +  |
| Tag         |  +  |  +  |  -  |  +  |
| Order       |  +  |  +  |  -  |  -  |
| User        |  -  |  +  |  -  |  -  |

## Set up 
- [*clone*](https://github.com/bakhridinova/gift-certificates-advanced.git) the project
- change [application.properties](controller/src/main/resources/application.properties) file based on your database configurations
- run the project using [GiftCertificatesAdvancedApplication.java](controller/src/main/java/com/epam/esm/GiftCertificatesAdvancedApplication.java) 

# API Reference 

## Get all entities

```agsl
request parameters:
    int page (defaultValue 1)
    int size (defaultValue 5)
```

**GET** `/api/certificates`
  returns list of certificates


**GET** `/api/orders`
  returns list of orders  


**GET** `/api/tags`
  returns list of tags  


**GET** `/api/users`
  returns list of users

## Get entity by id

**GET** `/api/certificates/{id}`
  returns certificate by given id if one exists


**GET** `/api/orders/{id}`
  returns order by given id if one exists


**GET** `/api/tags/{id}`
  returns tag by given id if one exists


**GET** `/api/users/{id}`
  returns user by given id if one exists

## Get orders

```agsl
request parameters:
    int page (defaultValue 1)
    int size (defaultValue 5)
```

**GET** `/api/certificates/{id}/orders`
returns orders of certificate by given id if one exists

**GET** `/api/users/{id}/orders`
returns orders of user by given id if one exists

## Get special tag

**GET** `/api/tags/special`
returns the most commonly used tag of a user with the highest cost of all orders


## Get certificates by filter
returns list of certificates filtered by passed parameters and tags

```agsl
request parameters:
    int page (defaultValue 1) 
    int size (defaultValue 5) 
```

```agsl
request body:
        {
            "name": "",
            "description": "",
            "sortType": "",
            "sortOrder": "",
            "tags": [
                {
                    "name": ""
                }
            ]
        }
```

## Create entity

**POST** `/api/certificates`
returns created certificate

```agsl
request body:
        {
            "name": "",
            "description": "",
            "price": 0,
            "duration": 0,
            "tags": [
                {
                    "name": ""
                }
            ] 
        }
```

**POST** `/api/orders`
returns created order

```agsl
request body:
        {
            "userId": 0,
            "certificateId": 0
        }
```

**POST** `/api/tags`
returns created tag

```agsl
request body:
        {
            "name": ""
        }
```

## Update entity

**PATCH** `/api/certificates/{id}`
returns updated certificate

```agsl
request body:
        {
            "name": ""
        }
```


## Delete entity
**DELETE** `/api/certificates/{id}`
deletes certificate by given id if one exists


**DELETE** `/api/tags/{id}`
deletes tag by given id if one exists

entity relationship diagram

<img src="https://user-images.githubusercontent.com/100201504/230478278-9b82ee50-bf41-4ef0-9324-d41755d5178b.png" alt=""/>