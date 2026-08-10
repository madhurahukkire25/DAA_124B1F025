import java.util.*;

 class Pair {
        int item;
        int wt;
        int profit;
        int prio;
        boolean type;
        Pair(int item, int wt, int profit, int prio, boolean type){
            this.item = item;
            this.profit = profit;
            this.wt = wt;
            this.type = type;
            this.prio = prio;
        }
 }
class FrationalKnapsack {
   
    public static void main(String[] args) {
        PriorityQueue<Pair> pq = new PriorityQueue<>((a,b) ->{
            if(a.prio != b.prio) return Integer.compare(a.prio, b.prio);
            if(a.prio == b.prio){
                double p1 = a.profit/a.wt;
                double p2 = b.profit/b.wt;
                return Double.compare(p2, p1);
            }
            return -1;
        });
       
        pq.add(new Pair(0, 10, 100, 1, false));
        pq.add(new Pair(1, 20, 60, 3, true));
        pq.add(new Pair(2, 30, 90, 2, true));
        pq.add(new Pair(3, 15, 40, 3, false));
        pq.add(new Pair(4, 5, 50, 1, false));
       
        int capacity = 50;
        int tprofit = 0;
        while(!pq.isEmpty()){
            Pair curr = pq.poll();
            if(curr.wt > capacity){
                if(curr.type == false) continue;
               
                tprofit += (curr.profit/curr.wt)*capacity;
                capacity = 0;
                break;
            }else{
                capacity -= curr.wt;
                tprofit += curr.profit;
            }
        }
       
        System.out.println(tprofit);
	//prints 255        
    }
}

