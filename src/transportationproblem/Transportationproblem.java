package transportationproblem;

import java.util.*;
import javax.swing.JOptionPane;

/**
 *
 * @author MEMO Byte
 */
public class Transportationproblem {
    double []required;
    double []stock;
    double [][]cost;
    LinkedList<Variable> feasible = new LinkedList<Variable>();
    
    int stockSize;    
    int requiredSize;    
    
   public Transportationproblem(int stockSize, int requiredSize) {
        this.stockSize = stockSize;
        this.requiredSize = requiredSize;
        
        stock = new double[stockSize];
        required = new double[requiredSize];
        cost = new double[stockSize][requiredSize];
        
        for(int i=0; i < (requiredSize + stockSize -1); i++)
            feasible.add(new Variable());
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void setStock(double value, int index){
        stock[index] = value;        
    }
    
    public void setRequired(double value, int index){
        required[index] = value;        
    }
    
    
    public void setCost(double value, int stock, int required){
        cost[stock][required] = value;
    }
    
    /**
     * initializes the feasible solution list using the North-West Corner
     * @return time elapsed
     */
    
    public double northWestCorner() {
        long start = System.nanoTime();
        
        double min;        
        int k = 0; //feasible solutions counter
        
        //isSet is responsible for annotating cells that have been allocated
        boolean [][]isSet = new boolean[stockSize][requiredSize];        
        for (int j = 0; j < requiredSize; j++)
            for (int i = 0;  i < stockSize; i++)
                    isSet[i][j] = false;
        
        //the for loop is responsible for iterating in the 'north-west' manner
        for (int j = 0; j < requiredSize; j++)
            for (int i = 0;  i < stockSize; i++)
                if(!isSet[i][j]){
                    
                    //allocating stock in the proper manner
                    min = Math.min(required[j], stock[i]);
                    
                    feasible.get(k).setRequired(j);
                    feasible.get(k).setStock(i);
                    feasible.get(k).setValue(min);
                    k++;

                    required[j] -= min;
                    stock[i] -= min;
                    
                    //allocating null values in the removed row/column
                    if(stock[i] == 0)
                        for(int l = 0; l < requiredSize; l++)
                            isSet[i][l] = true;                    
                    else
                        for(int l = 0; l < stockSize; l++)
                            isSet[l][j] = true;
                }
        return (System.nanoTime() - start) * 1.0e-9;
       
    }
    
    /**
     * initializes the feasible solution list using the Least Cost Rule
     * 
     * it differs from the North-West Corner rule by the order of candidate cells
     * which is determined by the corresponding cost
     * 
     * @return double: time elapsed
     */
    
    public double leastCostRule() {
        long start = System.nanoTime();

        double min;        
        int k = 0; //feasible solutions counter
        
        //isSet is responsible for annotating cells that have been allocated
        boolean [][]isSet = new boolean[stockSize][requiredSize];        
        for (int j = 0; j < requiredSize; j++)
            for (int i = 0;  i < stockSize; i++)
                    isSet[i][j] = false;
        
        int i = 0, j = 0;
        Variable minCost = new Variable();
        
        //this will loop is responsible for candidating cells by their least cost
        while(k < (stockSize + requiredSize - 1)){
            
            minCost.setValue(Double.MAX_VALUE);            
            //picking up the least cost cell          
            for (int m = 0;  m < stockSize; m++)
                for (int n = 0; n < requiredSize; n++)
                    if(!isSet[m][n])
                        if(cost[m][n] < minCost.getValue()){
                            minCost.setStock(m);
                            minCost.setRequired(n);
                            minCost.setValue(cost[m][n]);
                        }            
            
            i = minCost.getStock();
            j = minCost.getRequired();            
            
            //allocating stock in the proper manner
            min = Math.min(required[j], stock[i]);

            feasible.get(k).setRequired(j);
            feasible.get(k).setStock(i);
            feasible.get(k).setValue(min);
            k++;

            required[j] -= min;
            stock[i] -= min;
            
            //allocating null values in the removed row/column
            if(stock[i] == 0)
                for(int l = 0; l < requiredSize; l++)
                    isSet[i][l] = true;                    
            else
                for(int l = 0; l < stockSize; l++)
                    isSet[l][j] = true;

        }
        
       return (System.nanoTime() - start) * 1.0e-9;    
        
    }
    
    public double getSolution(){
        double result = 0;
        for(Variable x: feasible){
            result += x.getValue() * cost[x.getStock()][x.getRequired()];
        }
        
        return result;
    
    }
    public static void execution(){
        
          String q;
         
          JOptionPane.showMessageDialog(null,"Please enter the problem size:");
       
          q=JOptionPane.showInputDialog("Please enter the  stocks :");
           int s=Integer.parseInt(q);
           q=JOptionPane.showInputDialog("Please enter the  requirements:");
            int r=Integer.parseInt(q);
        double x;
     
        
        Transportationproblem test = new Transportationproblem(s,r);
        
     
        for (int i = 0; i < test.stockSize; i++){
             q=JOptionPane.showInputDialog("Please enter the stocks capacity ...for  "+(i+1)+"  stock");
              x=Integer.parseInt(q);
      
            test.setStock(x, i);
        } 
        for (int i = 0; i < test.requiredSize; i++){
            q=JOptionPane.showInputDialog("Please enter the requirements ...for  "+(i+1)+"  requirement");
              x=Integer.parseInt(q);
           
            test.setRequired(x, i);
        }
        for (int i = 0; i < test.stockSize; i++)
            for (int j = 0; j < test.requiredSize; j++) {
                q=JOptionPane.showInputDialog("Please enter the transportation costs ...for  "+(i)+""+(j));
              x=Integer.parseInt(q);
             
                test.setCost(x, i, j);
            }
        int in;
        do{
              
              String da;
                     do{
                          da=JOptionPane.showInputDialog("              (1) for northWestCorner \n"+           
                                                         "            (2) for leastCostRule \n"+
                                                         
                                                         "                          (0) Exit");
                          in=Integer.parseInt(da);
                     }while((in!=1)&&(in!=2)&&(in!=0));
                     switch(in)
                     {
                         case 1:
                            String KK="";
                                 test.northWestCorner();
                                 for(Variable t: test.feasible){
                                     KK +=""+t+"\n";
        }
        JOptionPane.showMessageDialog(null,"The basic variable\n"+KK+"Target function:\n"+test.getSolution());
      
                             
                             break;
                            
                         case(2):
                              String K="";
                                 test.leastCostRule();
                                 for(Variable t: test.feasible){
                                     K +=""+t+"\n";
        }
        JOptionPane.showMessageDialog(null,"The basic variable\n"+K+"Target function:\n"+test.getSolution());
      
                             break;
                      
                     }
                  }while(in!=0&&in!=1&&in!=2);
                  if (in==0)
                JOptionPane.showMessageDialog(null,"Thank you \n");
                      
            }
           
 }
        

    

