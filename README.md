# Smart DocX to Excel Converter

An automated backend web application built with **Java 17**, **Spring Boot**, **Apache POI**, and **PostgreSQL** that extracts tabular data from Word (`.docx`) documents and generates structured Excel (`.xlsx`) workbooks.

## Key Features
* **Automated Table Extraction:** Reads all tables inside an uploaded `.docx` file using Apache POI (`XWPFDocument`) and maps each table into a separate sheet in an `.xlsx` file (`XSSFWorkbook`).
* **RESTful API Architecture:** Built with Spring Boot to handle multipart file uploads (`POST /api/convert`), instant Excel downloads, and conversion history retrieval (`GET /api/history`).
* **PostgreSQL Audit Tracking:** Persists file metadata, extracted table counts, and conversion statuses in a PostgreSQL database using Spring Data JPA.
* **Built-in Web Interface:** Includes a lightweight frontend for instant file uploads and live history tracking.

## Tech Stack
* **Backend:** Java 17, Spring Boot 3, Spring Web, Spring Data JPA
* **Document Processing:** Apache POI (`poi-ooxml`)
* **Database:** PostgreSQL
* **Frontend:** HTML5, CSS3, JavaScript (Fetch API)
*
