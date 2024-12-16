# Periplus Cart Unit Test
This is a repository of a unit test to fulfill the requirement of the Openway Technical Test

## How to Run
If you are using Intellij IDE you can clone the repository and synchronize the test.
The IDE will automatically provide some automatic run button for the test

To run it from the terminal, cd to the root folder and type in this command
```
mvn test -Dtest=PeriplusTest
```

## Revision Changes
Based on the feedback in the technical interview, these are the changes that applied to the program to satisfy the requirements
| Changes | Description    | File Affected    |
| ---   | --- | :---: |
| Bug Fix Get Cart Items | Fix cart items not fully loaded but the program already tried to get it   |  PeriplusTest.class |
| Bug Fix Add to Cart | Fix when adding to cart often the program continues without waiting for the add function to register | PeriplusTest.class |
| Assertion on LoginTest | add assertion function to confirm the state of the login | PeriplusTest.class |
| Refactor with POM | Add POM for each page in the website to simplify and improve readability | CartPage.class, HomePage.class, LoginPage.class, ProductPage.class, PeriplusTest.class |
| Add Environment Variable | Change the use of constant from hardcoded to env to improve adaptability and run flexibility | .env |


For technical test purposes the .env file is included in the repository.
