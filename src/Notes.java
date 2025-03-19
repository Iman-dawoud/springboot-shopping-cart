
//application.properties
//spring.jpa.hibernate.ddl-auto = create //create table from scratch
//spring.jpa.hibernate.ddl-auto = update //update the table when there is any changes
//spring.jpa.hibernate.ddl-auto = create-drop //when the project is running it's going to create all the tables the schema, when the server is stopped it's going to drop the schema
//spring.jpa.hibernate.ddl-auto = validate //it is for production


//if you have a problem loading image for example the image you're trying to upload is too large you can add 2 configurations // Control the size of image you can upload in your system to avoid too much space consumption



// to convert Product to ProductDto we need ModelMapper so we need to serach in browser for 'ModelMapper springboot dependency' copy the dependency and add it to pom.xml here is the url https://modelmapper.org/user-manual/spring-integration/
//---------------------------------------------------------------------------------

//*in Cart class
// i asked my self why we did not use sum varivale to keep track of totalAmount so that we do not need to loop over all items each time we add a new one .
//keeping a running total (sum) seems like a better choice because it avoids iterating over all CartItems each time we add/remove an item.
//However, maintaining a separate sum variable can lead to serious consistency issues.
//Example of a Consistency Issue
//Suppose totalAmount is initially $100.
//A user removes a CartItem, but due to a bug, we forget to update totalAmount.
//Now, the total stored in the variable does not match the actual sum of prices in the cart.
//By recalculating using .stream() each time, we ensure accuracy and prevent incorrect totals.
//Preventing Side Effects from Manual Updates
//Maintaining a separate variable (sum) requires updating it every time a change happens (add, remove, modify quantity, change price).
//This means:
//Every method that modifies CartItem must explicitly update totalAmount.
//If one method forgets to update it, the total becomes wrong.
//Debugging incorrect totals is much harder than recalculating when needed.

//Modern Performance Optimizations Make It Fast
//The .stream().map().reduce() operation is optimized for performance.
//Iterating over a HashSet (or List) of CartItems is extremely fast, especially if there are only a few dozen items.
//The performance difference is negligible unless you have millions of cart items (which is unrealistic).

//Alternative: Lazy Calculation with Caching
//If performance really became an issue (which is rare), a better approach than sum would be:
//Cache the totalAmount and update it only when needed.
//Use an event-driven model, where updates are triggered by actual changes.

//*Note about orphanRemoval :
//orphanRemoval = true: When a CartItem is removed from the items set, Hibernate automatically deletes it from the database.
//How It Works:
//this.items.remove(item); → Removes item from the Set<CartItem> items.
//orphanRemoval = true detects that this CartItem is no longer referenced.Hibernate automatically deletes the CartItem from the database.


//*Why Do We Set item.setCart(null);?
//This is just an extra precaution to fully detach the CartItem from the Cart.
//Without this line, the item might still be in memory with a reference to the old cart (even though it’s being deleted from the database).
//By setting item.setCart(null);, we ensure that the CartItem is fully detached from the cart.


//*cartItemRepository.deleteByCartId(id)

//Hibernate should automatically delete all associated CartItems due to cascade = CascadeType.ALL. This means that calling cartItemRepository.deleteByCartId(id); is technically unnecessary.
//Why Might We Still Explicitly Delete CartItems?
//Even though cascade = CascadeType.ALL should handle this, some developers add cartItemRepository.deleteByCartId(id); for a few possible reasons:
//1.Database Performance & Query Optimization: When you directly delete CartItems first, it ensures that fewer constraints need to be checked by the database.
//Instead of having Hibernate figure out the dependencies and issue multiple SQL DELETE statements, an explicit bulk delete can be more efficient.
//2.Avoiding Potential Hibernate Issues: Hibernate doesn’t always execute deletes in the most optimal order.
//Some JPA implementations might run into issues with foreign key constraints if they first try to delete the Cart before the CartItems.
//Explicitly deleting CartItems first ensures there are no orphaned references in case of database constraints.
//3.Ensuring cart.getItems().clear(); Works Properly: If cart.getItems().clear(); is called before deleting the Cart, it removes all items from the items collection in memory.
//This ensures orphanRemoval = true triggers deletions even before the cart is deleted.
//In some cases, if the Cart is deleted first without clearing its items in memory, Hibernate may not detect orphaned entities properly.
//------------------------------------------------------------------------------------------------------------------------------------------


//* About getCart(Long id) method in CartService class
//Why Is cart.setTotalAmount(totalAmount); Being Used? At first glance, this line seems unnecessary, because:
//We are getting cart.getTotalAmount(), which already holds the total.
//Then, we set the same value back to cart.setTotalAmount(totalAmount), which doesn't change anything.
//Possible explanations:
//1.Forcing Hibernate to mark the entity as "dirty" (modified): Some JPA providers do not detect changes unless a setter is explicitly called. by calling setTotalAmount(), we are making sure Hibernate considers the entity "updated" and triggers a save.
//2.Handling lazy-loading issues: If totalAmount is a derived field (calculated from items in the cart), it might not be eagerly loaded. Calling getTotalAmount() ensures it's fully loaded before saving the cart again.
//3.Potential Bug / Redundant Code: If cart.setTotalAmount() is not actually modifying anything, this line might be redundant. The method should work fine without it, unless there's an issue with the calculation of totalAmount.
//Why Are We Saving the Cart Again?: Normally, just fetching an entity from the database should not require saving it again.If this line is included, it might be because totalAmount depends on other calculations,
//and the developer wanted to ensure that any modifications are persisted.
//When is this useful?: if totalAmount is computed based on cart items and sometimes becomes out of sync, this could be a way to recalculate and store the correct value.

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


//you can go to jwt.io to see what you have inside the token






