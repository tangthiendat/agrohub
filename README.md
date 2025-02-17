# AgroHub
AgroHub is a **management system for an agricultural supply warehouse chain**, designed to streamline operations, inventory tracking, sales, and customer management. The system follows an **event-driven microservices architecture**, ensuring scalability, resilience, and real-time data synchronization.

## Technologies Used
- **Backend:** Java 21, Spring Boot, Axon Framework
- **Frontend:** React 18, Typescript, Ant Design, Tailwind CSS
- **API Gateway:** Kong Gateway
- **Database & Storage:** MariaDB, PostgreSQL, Redis, Cloudinary
- **Messaging:**: Axon Server, Apache Kafka
- **Containerization:** Docker
- **Integration:** Email Server (Gmail/SMTP)

## Local Development Architecture
AgroHub is built using a **Spring Boot** microservices ecosystem and relies on **Axon Framework** for event sourcing, CQRS (Command Query Responsibility Segregation), and Saga pattern for distributed transaction management. The system also leverages **Kafka** for asynchronous messaging and Axon Server for event storage and query handling.

![Agrohub - Local Development Architecture](https://github.com/user-attachments/assets/bc83efac-e56e-45cd-8a2e-116898be4b49)

## Key Features
- **Event-Driven Architecture:** Uses Axon Framework with CQRS and Event Sourcing for reliable microservice interactions.
- **Role-Based Access Control**: Secure authentication and authorization with role and permission-based access.
- **Product & Customer Management**: Centralized product catalog and customer data with dynamic pricing and categorization.
- **Inventory & Supply Chain Management:** Tracks stock levels, automates warehouse transfers, and optimizes procurement workflows for efficiency.
- **Order & Sales Processing:** Manages sales orders, purchase orders, and fulfillment with event-driven consistency.
- **Debt & Financial Management:** Handles invoices, supplier payments, outstanding debts, and transaction reconciliation.
