package ui;
import java.util.*;
import java.time.*;
import entity.MenuItem;
import entity.Promotion;
import entity.Order;
import manager.OrderManager;
import manager.DatabaseManager;
import manager.SalesReportManager;
import database.Menu;
import database.TableList;
import database.StaffList;

public class OrderUI {
    
    public static void createOrder(){
        Menu menu = (Menu) DatabaseManager.fileRead("menu.bin"); 
        TableList tableList = (TableList) DatabaseManager.fileRead("table.bin");      
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        int selection = 0;
        MenuItem menuItem;
        Promotion promotion;
        ArrayList<MenuItem> menuItemArray = new ArrayList<MenuItem>();
        ArrayList<Promotion> promotionArray = new ArrayList<Promotion>();

        //use table number input to get the customer's phone number and member status
        System.out.println("Input table number:");
        int tableNumber = sc.nextInt();
        sc.nextLine();
        int customerPhoneNumber = tableList.getTableList().get(tableNumber).getPhoneNumber();
        boolean isMember = tableList.getTableList().get(tableNumber).isIsMember();

        //check for staff matching staffID and ensure that the staff is a waiter
        System.out.println("Input staff ID:");
        int staffID = sc.nextInt();
        int staffIDCheck = 0;
        sc.nextLine();
        StaffList staffList = (StaffList) DatabaseManager.fileRead("staff.bin");
        for(int i = 0; i < staffList.getStaffArray().size(); i++){
            if (staffList.getStaffArray().get(i).getStaffID() == staffID){
                if(staffList.getStaffArray().get(i).getJobTitle().equals("Waiter")){
                    staffIDCheck = 0;
                    break;
                }
            }
        }
        if(staffIDCheck == 0){
            System.out.println("We cannot get a waiter of matching staff ID! Order terminating...");
            return;
        }

        double totalPrice = 0;
        
        do{
            try{
                System.out.println("(1) Print Menu\n"
                                + "(2) Add Menu Item\n"
                                + "(3) Add Promotion\n"
                                + "(4) Remove Menu Item\n"
                                + "(5) Remove Promotion\n"
                                + "(6) View Order\n"
                                + "(7) Submit Order\n"
                                + "(8) Cancel Order\n"
                                + "Enter the number of your choice: ");
                choice = sc.nextInt();
                sc.nextLine();
                
                switch(choice){
                    case 1: //Print menu
                        MenuDisplay.menuPrintName();
                        break;
                    case 2: //Add menu item
                        MenuDisplay.menuItemPrintName();
                        System.out.println("Choose menu item to add to your order:");
                        selection = sc.nextInt();
                        sc.nextLine();
                        menuItem = menu.getMenuItemArray().get(selection-1);
                        menuItemArray.add(menuItem);
                        System.out.println(menuItem.getFoodName() + " added to your order!");
                        totalPrice += menuItem.getFoodPrice();
                        break;
                    case 3: //Add promotion
                        MenuDisplay.promotionPrintName();
                        System.out.println("Choose promotion to add to your order:");
                        selection = sc.nextInt();
                        sc.nextLine();
                        promotion = menu.getPromotionArray().get(selection-1);
                        promotionArray.add(promotion);
                        System.out.println(promotion.getPromotionName() + " added to your order!");
                        totalPrice += promotion.getPromotionPrice();
                        break;
                    case 4: //Remove menu item
                        OrderManager.orderMenuItemPrintNamePrice(menuItemArray);
                        if(menuItemArray.size() == 0)break; //If no menu item break here
                        System.out.println("Choose menu item to remove from your order:");
                        selection = sc.nextInt();
                        sc.nextLine();
                        menuItem = menuItemArray.remove(selection-1);
                        System.out.println(menuItem.getFoodName() + " removed!");
                        totalPrice -= menuItem.getFoodPrice();
                        break;
                    case 5: //Remove promotion
                        OrderManager.orderPromotionPrintNamePrice(promotionArray);
                        if(menuItemArray.size() == 0)break; //If no promotion break here
                        System.out.println("Choose promotion to remove from your order:");
                        selection = sc.nextInt();
                        sc.nextLine();
                        promotion = promotionArray.remove(selection-1);
                        System.out.println(promotion.getPromotionName() + " removed!");
                        totalPrice -= promotion.getPromotionPrice();
                        break;
                    case 6: //Print order
                        System.out.println("          OOP Restaurant          ");
                        System.out.println("**********************************");
                        System.out.println("Date: " + LocalDate.now() + ", Time: " + LocalTime.now());
                        System.out.println("Table Number: " + tableNumber);
                        OrderManager.orderMenuItemPrintNamePrice(menuItemArray);
                        System.out.println();
                        OrderManager.orderPromotionPrintNamePrice(promotionArray);
                        System.out.println("**********************************");
                        System.out.println("Subtotal price: $" + totalPrice);
                        System.out.println("Price after taxes: $" + (totalPrice*0.07));
                        //members get 10% off total bill
                        if(isMember){
                            System.out.println("Discount: $" + (totalPrice*0.1));
                        }
                        System.out.println("==================================");
                        if(isMember){
                            System.out.println("TOTAL DUE: " + (totalPrice*1.07-totalPrice*0.1));
                        }
                        else{
                            System.out.println("TOTAL DUE: " + (totalPrice*1.07));
                        }
                        System.out.println("==================================");
                        System.out.println("See you again soon!");
                        break;
                    case 7: 
                        //account for 10% off for members
                        if(isMember){
                            totalPrice *= 0.9;
                        }
                        Order order = new Order(menuItemArray, promotionArray, customerPhoneNumber, staffID, tableNumber, totalPrice, isMember);
                        SalesReportManager.updateSalesReport(order);
                        break;
                    case 8:
                        break;
                    default:
                        System.out.println("Input is out of range. Please try again...");
                }
            }catch(InputMismatchException e){
				System.out.println("Invalid choice (not an integer). Please try again...");
				if(sc.hasNextLine())sc.nextLine();
			}
        }while (choice != 8);

    }
}