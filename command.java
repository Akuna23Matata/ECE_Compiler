import java.util.List;

public class Command{
	String cmd;
	String[] op;

	public Command(String instr){
		String[] splitStr = instr.trim().split("\\s+");
		this.cmd = splitStr[0];
		String op[] = new String[(splitStr.length - 1)];
		for (int i = 1;i < splitStr.length ;i++ ) {
		 	op[i - 1] = splitStr[i];
		}
		this.op = op;
		for (int i = 0;i < this.op.length ;i++ ) {
			System.out.println(this.op[i]);
		}
	}

}