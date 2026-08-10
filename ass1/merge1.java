import java.util.*;
import java.io.*;
class Merge {

	static class TransactionRow {
		String or;
		String ts;
		TransactionRow(String or, String ts){
			this.or = or;
			this.ts  = ts;
		}
	}
	public static void main(String[] args){

		String inputFile = "transaction.csv";
		String sortFile = "sorted_file.csv";
		List<TransactionRow> list = new ArrayList<>();
		String headerText = "";
		try{
			Scanner sc = new Scanner(new File(inputFile));
			boolean isHeader = true;
			while(sc.hasNextLine()){
				String line = sc.nextLine();
				if(line.trim().isEmpty()) continue;
				if(isHeader){
					isHeader = false;
					continue;
				}

				String[] columns = line.split(",");
				String tmstp = columns[2].trim();
				list.add(new TransactionRow(line, tmstp));
			}
			
				
			
		}catch(Exception e){
			System.out.println(e.getMessage());
		}

		TransactionRow[] arr1 = new TransactionRow[list.size()];
                                for(int i = 0; i < list.size(); i++){
                                        arr1[i] = list.get(i);
                                }


	int[] arr = new int[]{7,3,5,6};
	 divide(arr1, 0, arr.length-1);


	 try
	 {
	 
	 	PrintWriter pw = new PrintWriter(new FileWriter(sortFile));
		if(!headerText.isEmpty()){
			pw.println(headerText);

		}
		for(TransactionRow row : arr1){
			pw.println(row.or);

		}

		System.out.println("sorted successfully");
	 }catch(Exception e){
	 	System.out.println(e.getMessage());
	 }


	}

	static void divide(TransactionRow[] arr, int l, int r){
		if(l == r) return;  
		int mid = l + (r-l)/2;
		divide(arr, l, mid);
		divide(arr, mid+1, r);

		merge(arr, l, mid, r);
	}

	static void merge(TransactionRow [] arr, int l, int mid, int r){
		TransactionRow[] ans = new TransactionRow[r-l+1];
		int i = l;
		int j = mid+1;
		int k = 0;
		while(i <= mid && j <= r){
			if(arr[i].ts.compareTo(arr[j].ts) <= 0){
				ans[k++] = arr[i];
				i++;
			}else{
				ans[k++] = arr[j];
				j++;
			}
		}

		while(i <= mid){
			ans[k++] = arr[i];
			i++;
		}

		while(j <= r){
			ans[k++] = arr[j];
			j++;
		}
		k = 0;
		for(int m = l ; m <= r; m++){
			arr[m] = ans[k++];
		}
	}
}
