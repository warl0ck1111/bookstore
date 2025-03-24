# Bookstore API

## Overview
The Bookstore API is a RESTful web service that allows users to manage books, authors, users, and orders. The API comes preloaded with data using the `DataLoader` class, which adds:
- **1 author**
- **10 books**
- **4 users**

### H2 Database Console
You can access the H2 database console at:  
[http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- **Username**: `sa`
- **Password**: `sa`

## API Endpoints

### Create an Author
#### Request
```http
POST http://localhost:8080/api/authors
Content-Type: application/json

{
  "name": "Charles Dickens"
}
```

---

### Create a Book
#### Request
```http
POST http://localhost:8080/api/v1/books
Content-Type: application/json

{
  "title": "David Copperfield",
  "genre": "FICTION",
  "isbn": "1-84205-167-9",
  "authorId": 3,
  "yearOfPublication": "1862",
  "stock": 10,
  "price": 2500
}
```

---

### Update Book Author
#### Request
```http
PUT http://localhost:8080/api/v1/books/1/author/3
Content-Type: application/x-www-form-urlencoded
```

---

### Change Author's Name
#### Request
```http
PUT http://localhost:8080/api/authors/1?name=test test
```

---

### Get Author by ID
#### Request
```http
GET http://localhost:8080/api/authors/1
```

---

### Create a User
#### Request
```http
POST http://localhost:8080/api/v1/users
Content-Type: application/json

{
  "username": "warl0ck1111",
  "password": "password",
  "confirmPassword": "password"
}
```

---

### Add Book to Cart
#### Request
```http
POST http://localhost:8080/api/cart/{{userId}}/add/{{bookId}}/2
```

---

### Remove Book from Cart
#### Request
```http
PUT http://localhost:8080/api/cart/{{userId}}/remove/{{bookId}}/{{quantity}}
Content-Type: application/x-www-form-urlencoded
```
#### Response
```json
{
  "status": "success",
  "data": null,
  "message": "Book quantity updated."
}
```

---

### Get Cart Details
#### Request
```http
GET http://localhost:8080/api/cart/{{userId}}
```
#### Response
```json
{
  "status": "success",
  "data": {
    "bookDetails": [
      {
        "book": {
          "id": 1,
          "title": "The Room on the Roof",
          "genre": "FICTION",
          "isbn": "1-84205-167-3",
          "authorId": 1,
          "yearOfPublication": 1863,
          "price": 2000.0,
          "stock": 10
        },
        "quantity": 10
      }
    ],
    "totalPrice": 20000.0
  },
  "message": ""
}
```

---

### Checkout Order
#### Request
```http
POST http://localhost:8080/api/checkout/{{userId}}?paymentMethod=WEB&paymentChannel=MOCK_PAYMENT_CHANNEL_2
```
#### Response
```json
{
  "status": "success",
  "data": {
    "orderId": 1,
    "customerId": 1,
    "totalPrice": 20000.0,
    "orderStatus": "PAID"
  },
  "message": ""
}
```

---

### Search for Books
#### Request
```http
GET http://localhost:8080/api/v1/books?
keyword=1-84205-167-9&
page=0&
pageSize=10&
sortField=title&
sortDirection=ASC
```
#### Response
```json
{
  "content": [
    {
      "id": 7,
      "title": "Angry River",
      "genre": "THRILLER",
      "isbn": "1-84205-167-9",
      "authorId": 1,
      "yearOfPublication": 1972,
      "price": 2300.0,
      "stock": 14
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "empty": false,
      "unsorted": false,
      "sorted": true
    },
    "offset": 0,
    "unpaged": false,
    "paged": true
  },
  "last": true,
  "totalPages": 1,
  "totalElements": 1,
  "first": true,
  "size": 10,
  "number": 0,
  "sort": {
    "empty": false,
    "unsorted": false,
    "sorted": true
  },
  "numberOfElements": 1,
  "empty": false
}
```

## Setup and Running the Application
### Prerequisites
- Java 21
- Maven

### Running the Application
```sh
mvn spring-boot:run
```

## Notes
- The application is preloaded with test data to facilitate immediate testing.
- Use the H2 console to inspect or modify database records.

