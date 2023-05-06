package cs_teamproject;
import java.io.*; 
import java.util.*;

class FileInputModule {
   private int[] Blocks = new int[10000];
   private int[] fitBlocks;
   private int index;

   public int[] fileInputList() throws IOException { // 파일내부의 모든 수를 전부 받고, 받은 수 만큼의 배열을 할당해서 반환하는 함수
      Scanner input = new Scanner(new File("input.txt"));
      while (input.hasNext()) {
         Blocks[this.index++] = input.nextInt();
      }
      fitBlocks = new int[index];
      for (int i = 0; i < fitBlocks.length; i++) {
         fitBlocks[i] = Blocks[i];
      }
      return fitBlocks;
   }
}

public class LFU { // LFU 알고리즘 클래스
   private int fileIndex = 0;
   private int hit = 0;
   private int count = 0;

   public static void increaseIndex(Node[] node) {
      for (int i = 0; i < node.length; i++) {
         if (node[i] != null)
            node[i].stackIndex();
      }
   }

   public int same(Node[] node, int num) { // 노드 안에 현재 받은 숫자와 일치하는 숫자가 있는지 검사하는 메소드
      int sameCount = 0;
      for (int i = 0; i < node.length; i++) {
         if (node[i] == null)
            continue;
         else if (node[i].getNumber() == num)
            sameCount++;
      }
      return sameCount;
   }

   public int compare(Node[] node) { // 사용빈도가 낮은 것 중에서 가장 오래 참조된 노드를 찾아주는 메소드
      int compareNum = node[0].getCount();
      int compare = 0;
      int compareIndex = node[0].getIndex();
      for (int i = 0; i < node.length; i++) {
         if (compareNum > node[i].getCount()) {
            compareNum = node[i].getCount();
            compareIndex = node[i].getIndex();
            compare = i;
         } else if (compareNum == node[i].getCount() && compareIndex < node[i].getIndex()) {
            compareIndex = node[i].getIndex();
            compare = i;
         }
      }
      return compare;
   }

   public int LFU(Node[] node, int num[]) { // LFU 알고리즘 구현
      for (int i = 0; i < num.length; i++) {
         if (num[i] == 99) {
            fileIndex = i; // i번째 숫자가 99이므로 적중률의 분모는 i
            break;
         }
         for (int n = 0; n < node.length; n++) {
            if (node[n] == null) { // 값을 넣을 곳이 비어있을 경우
               increaseIndex(node);
               node[n] = new Node(node, num[i]);
               count++;
               n = node.length;
               break;
            } else if (node[n].getNumber() == num[i]) { // Hit 했을경우
               increaseIndex(node);
               node[n].setIndex(1); // 인덱스 초기화
               node[n].Increase(); // 카운트 증가
               hit++;
               n = node.length;
               break;
            } else if (count == node.length && same(node, num[i]) == 0) { // 칸이 모두 차있고 같은 값이 없을경우
               increaseIndex(node);
               node[compare(node)] = new Node(node, num[i]);
               n = node.length;
            }
         }
      }
      return hit;
   }

   public LFU(int index, int[] num) {
      Node[] node = new Node[index];
      int Hit = LFU(node, num);
      for (int i = 0; i < node.length; i++) {
         System.out.print(node[i].getNumber() + " ");
      }
      System.out.println();
      System.out.println("LFU 알고리즘 Hit 적중률 : " + Hit + "/" + fileIndex); // i = fileIndex
   }

   public static void main(String[] args) throws IOException {
      FileInputModule input = new FileInputModule();
      new LFU(4, input.fileInputList());
   }
}

class Node {
   private int count; // 중복 숫자 count
   private int num;
   private int index = 0;

   public Node(Node[] node, int num) {
      this.count = 1;
      this.num = num;
   }

   public void setCount(int count) {
      this.count = count;
   }

   public void setIndex(int index) {
      this.index = index;
   }

   public int getCount() {
      return count;
   }

   public int getIndex() {
      return index;
   }

   public void Increase() {
      this.count++;
   }

   public void stackIndex() {
      this.index++;
   }

   public int getNumber() {
      return num;
   }
}