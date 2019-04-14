
# Trendyol Shopping Cart
This project implements second case of codility assignment. Since some of the java8 functionality used, you need java8 to run the project.

## Design Decisions
- campaign applied to category, not to shopping cart directly.
- instead of DiscountType, campaigns and coupons implements interfaces.
- to achieve dynamic cost calculation, we use DeliveryCostCalculator and any new cost calculator should implement it.

## Testing
Test Driven Development methodology followed during development of this project. You can test project with jUnit4.

### PIT
Pitest mutation testing tool applied and all defects fixed.

## Docs
Since code is over documentation, we use tests as our docs. 
