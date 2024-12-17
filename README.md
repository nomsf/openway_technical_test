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

## Test Suite Documentation
Below are the test suite documentation that contaion information about the test cases

### Test Case 1
| **Name**              | Login()                                             |
|-----------------------|-----------------------------------------------------|
| **Objective**         | To test the account authentication and validity     |
| **Steps**             | - Navigate to login page<br>- Enter login credentials |
| **Expected Result**   | The login is authenticated and redirected                       |
| **Actual Result**     | The login is authenticated and redirected                               |
| **Score**             | SUCCESS                                                   |

### Test Case 2
| **Name**              | AddItemToCart()                                             |
|-----------------------|-----------------------------------------------------|
| **Objective**         | To confirm the functionality of the quick add-to-cart button in the product web element      |
| **Steps**             | - Choose a single product in the homepage<br>- Click on the add to cart button that pops up<br>- Confirm by checking the number of item in cart icon<br>- Go to cart page and confirm the item |
| **Expected Result**   | The number and quantity of the cart items match the ordered value |
| **Actual Result**     | The item and quantity match with wait time in add-to-cart function to complete |
| **Score**             | SUCCESS                                               |

### Test Case 3
| **Name**              | AddMultipleItemsToCart()                                             |
|-----------------------|-----------------------------------------------------|
| **Objective**         | To confirm the functionality of the quick add-to-cart button in the product web element in more than one product      |
| **Steps**             | - Choose a numerous product on the homepage<br>- Click on the add to cart button that pops up for each of the items <br>- Confirm by checking the number of items in cart icon<br>- Go to the cart page and confirm the item |
| **Expected Result**   | The number and quantity of the cart items match the ordered value |
| **Actual Result**     | The item and quantity match with wait time in add-to-cart function to complete |
| **Score**             | SUCCESS                                               |

### Test Case 4
| **Name**              | AddQuantityItemsToCart()                                             |
|-----------------------|-----------------------------------------------------|
| **Objective**         | Test the quantity input field to order multiple of the same items |
| **Steps**             | - Choose a single product in the homepage<br>- Go to said product page<br>- Input the number of quantity in the input fields<br>- Click add-to-cart button <br>- Confirm by checking the number of items in cart icon<br>- Go to cart page and confirm the item |
| **Expected Result**   | The number and quantity of the cart items match the ordered value |
| **Actual Result**     | The item and quantity match  |
| **Score**             | SUCCESS                                               |

### Test Case 5
| **Name**              | AddMultipleQuantityItemsToCart()                    |
|-----------------------|-----------------------------------------------------|
| **Objective**         | Test the quantity input field to order multiple of the same items on multiple items |
| **Steps**             | - Choose a numerous product on the homepage<br>- Go to  each said product page<br>- Input the number of quantity in the input fields in each product page<br>- Click add-to-cart button <br>- Confirm by checking the number of items in cart icon<br>- Go to the cart page and confirm the item |
| **Expected Result**   | The number and quantity of the cart items match the ordered value |
| **Actual Result**     | The item and quantity match  |
| **Score**             | SUCCESS                                               |

### Test Case 6
| **Name**              | EdgeValueOrder()                    |
|-----------------------|-----------------------------------------------------|
| **Objective**         | Test the edge cases where the quantity input in the product page is unsupported or prohibited value |
| **Steps**             | - Choose a single product on the homepage<br>- Go to said product page<br>- Input the number of quantity of 0 and 101 (prohibited values) in the input fields<br>- Click add-to-cart button<br>- Check for warning sign and functionality of the add-to-cart function  |
| **Expected Result**   | The warning is displayed and cannot continue to add to the cart |
| **Actual Result**     | Warning alert is displayed and add to cart function is cancelled  |
| **Score**             | -                                               |

### Test Case 7
| **Name**              | StockLimitOrder()                    |
|-----------------------|-----------------------------------------------------|
| **Objective**         | Test the case where quantity inputted in the field exceed the product stock |
| **Steps**             | - Choose a single product on the homepage<br>- Go to said product page<br>- Input the number of quantity of 100<br>- Click add-to-cart button<br>- Check for warning sign and functionality of the add-to-cart function  |
| **Expected Result**   | The warning is displayed and cannot continue to add to the cart |
| **Actual Result**     | Warning alert is displayed and add to cart function is cancelled  |
| **Score**             | -                                               |


