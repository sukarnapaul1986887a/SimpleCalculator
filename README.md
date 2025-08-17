# SimpleCalculator
This repository named SimpleCalculator contains the code for assignment 1 of Distributed System course. This is prepared by Sukarna Paul with student id a1986887.

This project will help to gain an understanding of how java RMI works.

## How to Compile and Run

### 1. Compile
```bash
javac *.java
```

### 2. Start RMI Registry
```bash
rmiregistry 1099 &
```

### 3. Run Server
```bash
java CalculatorServer
```

### 4. Run Client
```bash
java CalculatorClient
```

### 5. Run Multi-Client
```bash
java CalculatorClient multi 5
```