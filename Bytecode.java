import java.util.*;

public class Bytecode {
    public static final int CMPE = 132;
    public static final int CMPLT = 136;
    public static final int CMPGT = 140;
    public static final int JMP = 36;
    public static final int JMPC = 40;
    public static final int PUSHI = 70;
    public static final int PUSHVI = 74;
    public static final int POPM = 76;
    public static final int POPV = 80;
    public static final int POPA = 77;
    public static final int PEEKI = 86;
    public static final int POKEI = 90;
    public static final int SWP = 94;
    public static final int ADD = 100;
    public static final int SUB = 104;
    public static final int MUL = 108;
    public static final int DIV = 112;
    public static final int PRINTI = 146;

    private int sc = 0;
    private int pc = -1; // program counter
    private int fo = -1; // # of local variables

    private ArrayList<String> input = new ArrayList<String>();
    private ArrayList<Integer> memory = new ArrayList<Integer>();
    private Map<String, Integer> symbol_table = new HashMap<>();

    public Bytecode(ArrayList<String> src) {
        input = src;
    }

    private void decl(String str) {
        symbol_table.put(str, ++fo);
        pushi(0);
    };

    private void lab(String str) {
        symbol_table.put(str, pc + 1);
    };

    private void subr(int i, String str) {
        pushi(16);
        pushi(17);
        pushi(1);
        memory.add(0x2C);
        memory.add(0);
        pc+=2;
    };

    private static Integer[] intToByteInt(int val){
        return new Integer[] {
                 (val & 0xFF000000) >> 24,  (val & 0x00FF0000) >> 16,  (val & 0x0000FF00) >> 8,  (val & 0x000000FF)
        };
    }

    private void printi(int num){
        Integer[] bytes = intToByteInt(num);
        memory.add(PUSHI);
        memory.add(bytes[3]);
        memory.add(bytes[2]);
        memory.add(bytes[1]);
        memory.add(bytes[0]);
        memory.add(PRINTI);
        pc += 6;
    };

    private void printv(String str){
        int offset = symbol_table.get(str);
        pushi(offset);
        memory.add(PUSHVI);
        memory.add(PRINTI);
        pc+=2;
    };

    private void jmp(String str) {
        int offset = 0;
        if (symbol_table.containsKey(str)) {
            offset = symbol_table.get(str);
        }
        pushi(offset);
        memory.add(JMP);
        pc++;
    };

    private void jmpc(String str) {
        int offset = 0;
        if (symbol_table.containsKey(str)) {
            offset = symbol_table.get(str);
        }
        pushi(offset);
        memory.add(JMPC);
        pc++;
    };

    private void cmpe() {
        memory.add(CMPE);
        pc++;
    };

    private void cmplt() {
        memory.add(CMPLT);
        pc++;
    };

    private void cmpgt() {
        memory.add(CMPGT);
        pc++;
    };

    private void pushv(String str){
        int offset = symbol_table.get(str);
        pushi(offset);
        memory.add(PUSHVI);
        pc++;
    };

    private void pushi(int i){
        Integer[] bytes = intToByteInt(i);
        memory.add(PUSHI);
        memory.add(bytes[3]);
        memory.add(bytes[2]);
        memory.add(bytes[1]);
        memory.add(bytes[0]);
        pc+=5;
    };

    private void popm(int i) {
        pushi(i);
        memory.add(POPM);
    };

    private void popa() {
        pushi(0);
        memory.add(POPA);
        pc++;
    };

    private void popv(String str) {
        int offset = symbol_table.get(str);
        pushi(offset);
        memory.add(POPV);
        pc++;
    };

    private void peek(String str, int i){
        int offset = symbol_table.get(str);
        pushi(offset);
        pushi(i);
        memory.add(PEEKI);
        pc++;
    };

    private void poke(int i , String str) {
        int offset = symbol_table.get(str);
        pushi(offset);
        pushi(i);
        memory.add(POKEI);
        pc++;
    };

    private void swp(){
        memory.add(SWP);
        pc++;
    };

    private void add(){
        memory.add(ADD);
        pc++;
    };

    private void sub() {
        memory.add(SUB);
        pc++;
    };

    private void mul() {
        memory.add(MUL);
        pc++;
    };

    private void div() {
        memory.add(DIV);
        pc++;
    };

    private void parse(String stmt){
        if (stmt.isEmpty() || stmt.substring(0, 2).equals("//")) {
            return;
        }
        String[] command = stmt.split(" ");
        String operation = command[0];
        switch (operation) {
            case("subr"):
                this.subr(Integer.parseInt(command[1]), command[2]);
                break;
            case("decl"):
                this.decl(command[1]);
                break;
            case("pushi"):
                this.pushi(Integer.parseInt(command[1]));
                break;
            case("printi"):
                this.printi(Integer.parseInt(command[1]));
                break;
            case("peek"):
                this.peek(command[1], Integer.parseInt(command[2]));
                break;
            case("poke"):
                this.poke(Integer.parseInt(command[1]), command[2]);
                break;
            case("popv"):
                this.popv(command[1]);
                break;
            case("pushv"):
                this.pushv(command[1]);
                break;
            case("printv"):
                this.printv(command[1]);
                break;
            case("lab"):
                this.lab(command[1]);
                break;
            case("jmp"):
                this.jmp(command[1]);
                break;
            case("jmpc"):
                this.jmpc(command[1]);
                break;
            case("popm"):
                this.popm(Integer.parseInt(command[1]));
                break;
            case("add"):
                this.add();
                break;
            case("sub"):
                this.sub();
                break;
            case("mul"):
                this.mul();
                break;
            case("div"):
                this.div();
                break;
            case("cmpe"):
                this.cmpe();
                break;
            case("cmplt"):
                this.cmplt();
                break;
            case("cmpgt"):
                this.cmpgt();
                break;
            case("swp"):
                this.swp();
                break;
            case("ret"):
                this.popa();
                memory.add(48);
                pc++;
                break;
            default:
                System.out.println("Command not Found");
        }
    };

    ArrayList<Integer> compile() {
        int i = 0;
        memory = new ArrayList<>();
        for (String str : input) {
            sc = i + 1;
            parse(str);
        }
        return memory;
    };
}
