CREATE TABLE IF NOT EXISTS orders (
                                      id INT AUTO_INCREMENT PRIMARY KEY,
                                      orderNumber VARCHAR(255) NOT NULL,
    totalOrderAmount INT NOT NULL,
    orderDate DATE NOT NULL,
    address VARCHAR(255) NOT NULL,
    deliveryAddress VARCHAR(255) NOT NULL,
    paymentType VARCHAR(50) NOT NULL,
    deliveryType VARCHAR(50) NOT NULL
    );

CREATE TABLE IF NOT EXISTS ordersdetails (
                                             id INT AUTO_INCREMENT PRIMARY KEY,
                                             articleProduct INT NOT NULL,
                                             nameProduct VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    unitCost INT NOT NULL,
    idOrders INT NOT NULL,
    FOREIGN KEY (idOrders) REFERENCES orders(id)
    );