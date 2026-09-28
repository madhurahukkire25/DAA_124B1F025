// Online Java Compiler (Editor)
// Write and run Java online using this editor.

class Main {
    public static void main(String[] args) {
        int[][] arr = {
            {2,1},{3,2},{4,5}
    };
        int c = 6;
        int[][]dp = new int[arr.length+1][c+1];

        for(int i = 0; i <= arr.length; i++){
            for(int j = 0; j <= c; j++){
                if(i == 0 || j == 0){
                    dp[i][j] = 0;
                }else{
                    if(arr[i-1][0] > j){
                        dp[i][j] = dp[i-1][j];
                    }else{
                        dp[i][j] = arr[i-1][1] + dp[i-1][j-arr[i-1][0]];
                    }
                }
            }
        }


        for(int i = 0; i < dp.length; i++){
            for(int j = 0; j < dp[0].length; j++){
                System.out.print(dp[i][j] + " ");
            }
            System.out.println(" ");
        }

        
        int i = arr.length;
        while(c > 0 && i != 0){
            if(dp[i][c] != dp[i-1][c]){
                System.out.println(i-1);
                c -= arr[i-1][0];
            }
            i--;
        }
        
    }

