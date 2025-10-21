import java.io.*;
import java.util.*;

/* ResourceManagement
 *
 * Stores the information needed to decide which items to purchase for the given budget and departments
 */
public class ResourceManagement
{
    private PriorityQueue<Department> departmentPQ; /* priority queue of departments */
    private Double remainingBudget;                 /* the budget left after purchases are made (should be 0 after the constructor runs) */
    private Double budget;
    /* TO BE COMPLETED BY YOU
   * Fill in your name in the function below
   */  
  public static void printNames( )
  {
    /* TODO : Fill in your name and your partner's name */
    System.out.println("This solution was completed by:");
    System.out.println("Janet Yeboah");
    System.out.println("<student name #2 (if no partner write \"N/A\")>");
  }

  /* Constructor for a ResourceManagement object
   * TODO
   * Simulates the algorithm from the pdf to determine what items are purchased
   * for the given budget and department item lists.
   */
  public ResourceManagement( ArrayList<String> fileNames, Double budget )
  {
    /* Create a department for each file listed in fileNames */
    departmentPQ = new PriorityQueue<>();
    this.remainingBudget = budget;

      for ( String filename : fileNames) {
        Department dept = new Department(filename);
        departmentPQ.add(dept);
      }
    

  }
  
  /* Provided code - DO NOT CHANGE THIS METHOD 
   * findAndPrintBudget
   * Executes the budget distribution algorithm and prints a record of which items were purchased
   */    
  public void findAndPrintBudget(  ){
    /* Simulate the algorithm from the PDF */
    allocateBudget(  );
    System.out.print( "\n\n" );
    
    /* Summary of how the budget was distributed to the departments */
    printSummaryOfDistribution(  );
  } 

  /* allocateBudget
   * TODO
   * Executes the budget distribution algorithm and prints a record of which items were purchased
   */    
  private void allocateBudget(  ){
    /* Simulate the algorithm for picking the items to purchase */
    /* Print a record of each item as they are purchased (see PDF and sample output for example tables) */
    double scholarship;
    Item scholarShipItem = null;
    Item purchasedItem = null;
    System.out.println("ITEMS PURCHASED");
    System.out.println("----------------------------");
    while(!departmentPQ.isEmpty() && remainingBudget > 0){
      Department dept = departmentPQ.poll();

      while(!dept.itemsDesired.isEmpty() && dept.itemsDesired.peek().price > remainingBudget){
        dept.itemsRemoved.add(dept.itemsDesired.poll());
      }

      if(dept.itemsDesired.isEmpty()){
        if(remainingBudget < 1000.0){
          scholarship = remainingBudget; 
        }else{
          scholarship = 1000.0;
        }

        if(scholarship > 0){
          scholarShipItem = new Item("Scholarship", scholarship);
          dept.itemsReceived.add(scholarShipItem);
          dept.priority = dept.priority + scholarship;
          remainingBudget = remainingBudget - scholarship;
        }
        System.out.printf("%-40s - %-30s - $%10.2f", "Department of " + dept.name, scholarShipItem.name, scholarShipItem.price);
      } else{
          purchasedItem = dept.itemsDesired.poll();
          dept.itemsReceived.add(purchasedItem);
          dept.priority = dept.priority + purchasedItem.price;
          remainingBudget = remainingBudget - purchasedItem.price;

          System.out.printf("%-40s - %-30s - $%10.2f", "Department of " + dept.name, purchasedItem.name, purchasedItem.price);

      }

      if(!dept.itemsDesired.isEmpty()){
        departmentPQ.add(dept);
      }
    }
    
    System.out.println("\nBudget allocation complete!");
  } 

  /* printSummaryOfDistribution
   * TODO
   * Print a summary of what each department received and did not receive
   */    
  private void printSummaryOfDistribution(  ){

  } 
}

/* Department
 *
 * Stores the information associated with a Department at the university
 */
class Department implements Comparable<Department>
{
  String name;                /* name of this department */
  Double priority;            /* total money spent on this department */
  Queue<Item> itemsDesired;   /* list of items this department wants */
  Queue<Item> itemsReceived;  /* list of items this department received */
  Queue<Item> itemsRemoved;   /* list of items that were skipped because they exceeded the remaining budget */

  /* TODO
   * Constructor to build a Department from the information in the given fileName
   */
  public Department( String fileName ){
    /* Open the fileName, create items based on the contents, and add those items to itemsDesired */
    File file = new File(fileName);
    Scanner input;
    try{
      input = new Scanner(file);
      this.name = input.nextLine();
      this.priority = 0.0;

      this.itemsDesired = new LinkedList<>();
      this.itemsReceived = new LinkedList<>();
      this.itemsRemoved = new LinkedList<>();

      while(input.hasNext()){
        String itemsNeeded = input.next();
        if(!input.hasNextDouble()){
            break;
        }
        double priceOfItem = input.nextDouble();
        itemsDesired.add(new Item(itemsNeeded, priceOfItem));
      }
      input.close();
    }catch(Exception e){
      e.printStackTrace();
      System.err.println("The file" +fileName+ "was not found");
      return;
    }
  }
    
  /*
   * Compares the data in the given Department to the data in this Department
   * Returns -1 if this Department comes first
   * Returns 0 if these Departments have equal priority
   * Returns 1 if the given Department comes first
   *
   * This function is to ensure the departments are sorted by the priority when put in the priority queue 
   */
  public int compareTo( Department dept ){
    return this.priority.compareTo( dept.priority );
  }

  public boolean equals( Department dept ){
    return this.name.compareTo( dept.name ) == 0;
  }

  @Override 
  @SuppressWarnings("unchecked") //Suppresses warning for cast
  public boolean equals(Object aThat) {
    if (this == aThat) //Shortcut the future comparisons if the locations in memory are the same
      return true;
    if (!(aThat instanceof Department))
      return false;
    Department that = (Department)aThat;
    return this.equals( that ); //Use above equals method
  }
  
  @Override
  public int hashCode() {
    return name.hashCode(); /* use the hashCode for data stored in this name */
  }

  /* Debugging tool
   * Converts this Department to a string
   */		
  public String toString() {
    return "NAME: " + name + "\nPRIORITY: " + priority + "\nDESIRED: " + itemsDesired + "\nRECEIVED " + itemsReceived + "\nREMOVED " + itemsRemoved + "\n";
  }
}

/* Item
 *
 * Stores the information associated with an Item which is desired by a Department
 */
class Item
{
  String name;    /* name of this item */
  Double price;   /* price of this item */

  /*
   * Constructor to build a Item
   */
  public Item( String name, Double price ){
    this.name = name;
    this.price = price;
  }

  /* Debugging tool
   * Converts this Item to a string
   */		
  public String toString() {
    return "{ " + name + ", " + price + " }";
  }
}
