# Wallet System API

A simple wallet service built with Spring Boot that allows users to create accounts, fund wallets, and transfer money between accounts. The goal of this project is to demonstrate clean service-layer logic, event-driven account creation, and safe transaction handling.

---

## Overview

This project simulates a basic digital wallet.
A user can:

* Create an account
* Automatically get a wallet
* Fund the wallet
* Transfer funds to another account
* Prevent duplicate transfers using idempotency keys

When a user is created, an event is published that can be handled elsewhere in the system to create the account and wallet.

---

## Tech Stack

* Java
* Spring Boot
* Spring Data JPA
* MySQL
* Lombok
* Maven

---

## Project Structure

```
com.collins.Wallet.System
│
├── controller
├── service
│   └── Impl
├── repository
├── model
├── dtos
├── mapper
├── event
├── enums
└── exception
```

---

## Main Features

### Create User

Creates a new wallet user and publishes a `UserCreatedEvent`.

**Endpoint**

```
POST /api/v1/wallets/create_user
```

**Request Example**

```json
{
  "fullName": "Collins Okafor",
  "email": "collinsdaberechi20@gmail.com"
}
```

---

### Fund Account

Adds money to an existing wallet.

**Endpoint**

```
POST /api/v1/wallets/transfer_funds
```

**Request Example**

```json
{
  "accountNumber": "5020915812",
  "amount": 5000
}
```

---

### Transfer Funds

Transfers money from one wallet account to another.

**Endpoint**

```
POST /api/v1/wallets/fund_account
```

**Header**

```
Idempotency-Key: unique-key-{{vault:authorization-secret---uuid}}
```

**Request Example**

```json
{
  "sourceAccount": "5020915812",
  "destinationAccount": "5213775022",
  "amount": 1000
}
```

The idempotency key prevents duplicate transfers if the same request is sent more than once.

---

## Idempotency Handling

Each transfer request is saved with a unique key.
If a request with the same key is received again and already completed, the system rejects it to avoid duplicate transactions.

---

## Running the Project

1. Clone the repository

```
git clone https://github.com/collinsdaberechukwu/wallet-system.git
```

2. Configure your database in `application.yml`

```
spring.datasource.url=jdbc:mysql://localhost:3306/walletSystemDb
spring.datasource.username=root
spring.datasource.password= {password}
```

3. Run the application

```
mvn spring-boot:run
```

The server will start on:

```
http://localhost:9081
```

---

## Future Improvements

Some things that could be added later:

* Transaction history
* API documentation with Swagger


---

## Author

Okafor Collins Daberechukwu

Backend Developer (Java / Spring Boot)

---
