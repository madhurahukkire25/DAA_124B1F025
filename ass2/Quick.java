class Quick {
	public static void main(String[] args){
		int[] arr = new int[]{2,1,3,6,4,5,7};
		solve(arr, 0, arr.length-1);
		for(int a : arr){
			System.out.println(a);
		}
	}

	static void solve(int[] arr, int low, int high){
		if(low >= high) return;
		int pivotIdx = partitionIdx(arr, low, high);
		solve(arr, low, pivotIdx-1);
		solve(arr, pivotIdx+1, high);
	}

	static int partitionIdx(int[] arr, int low, int high){
		int i = low-1;
		int j = low;
		int pivot = arr[high];
		while(j <= high){
			if(arr[j] <= pivot){
				i++;
				int temp = arr[i];
				arr[i] = arr[j];
				arr[j] = temp;
			}
			j++;
		}

		return i;


	}
}

