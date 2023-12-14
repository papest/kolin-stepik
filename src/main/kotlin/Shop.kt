data class Shop(val name: String, val customers: List<Customer>)

data class Customer(val name: String, val city: City, val orders: List<Order>)

data class City(val name: String)

data class Order(val products: List<Product>, val isDelivered: Boolean)

data class Product(val name: String, val price: Double)

fun Shop.getSetOfCustomers(): Set<Customer> = customers.toSet()

// Return the set of cities the customers are from
fun Shop.getCitiesCustomersAreFrom(): Set<City> = customers.map { it.city }.toSet()

// Return a list of the customers who live in the given city
fun Shop.getCustomersFrom(city: City): List<Customer> = customers.filter { it.city == city }

// Return true if all customers are from the given city
fun Shop.checkAllCustomersAreFrom(city: City): Boolean = customers.all { it.city == city }

// Return true if there is at least one customer from the given city
fun Shop.hasCustomerFrom(city: City): Boolean = customers.any { it.city == city }

// Return the number of customers from the given city
fun Shop.countCustomersFrom(city: City): Int = customers.count { it.city == city }

// Return a customer who lives in the given city, or null if there is none
fun Shop.findAnyCustomerFrom(city: City): Customer? = customers.find { it.city == city }

// Return all products this customer has ordered
fun Customer.getOrderedProducts(): Set<Product> = orders.flatMap { it.products }.toSet()

// Return all products that were ordered by at least one customer
fun Shop.getAllOrderedProducts(): Set<Product> = customers.flatMap { it.getOrderedProducts() }.toSet()

// Return a customer whose order count is the highest among all customers
fun Shop.getCustomerWithMaximumNumberOfOrders(): Customer? = customers.maxByOrNull { it.orders.count() }

// Return the most expensive product which has been ordered
fun Customer.getMostExpensiveOrderedProduct(): Product? = orders.flatMap { it.products }.maxByOrNull { it.price }

// Return a list of customers, sorted by the ascending number of orders they made
fun Shop.getCustomersSortedByNumberOfOrders(): List<Customer> = customers.sortedBy { it.orders.count() }

// Return the sum of prices of all products that a customer has ordered.
// Note: the customer may order the same product for several times.
fun Customer.getTotalOrderPrice(): Double = orders.sumOf { it.products.sumOf { prod -> prod.price } }

// Return a map of the customers living in each city
fun Shop.groupCustomersByCity(): Map<City, List<Customer>> = customers.groupBy { it.city }

// Return customers who have more undelivered orders than delivered
fun Shop.getCustomersWithMoreUndeliveredOrdersThanDelivered(): Set<Customer> = customers.filter {
    val (delivered, undelivered) = it.orders.partition { order -> order.isDelivered }
    delivered.size < undelivered.size
}.toSet()

// Return the set of products that were ordered by every customer
fun Shop.getSetOfProductsOrderedByEveryCustomer(): Set<Product> {
    val allProducts = customers.flatMap { it.orders }.flatMap { order -> order.products }.toSet()
    return customers.map { it.orders.flatMap { order -> order.products }.toSet() }
        .fold(allProducts, { element, partIntersect -> partIntersect.intersect(element) })
}

// Return the most expensive product among all delivered products
// (use the Order.isDelivered flag)
fun Customer.getMostExpensiveDeliveredProduct(): Product? = orders.filter { it.isDelivered }.flatMap { it.products }
    .maxByOrNull { it.price }

// Return how many times the given product was ordered.
// Note: a customer may order the same product for several times.
fun Shop.getNumberOfTimesProductWasOrdered(product: Product): Int =
    customers.flatMap { it.orders }.count { it.products.contains(product) }