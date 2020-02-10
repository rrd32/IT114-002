import java.awt.List;
import java.util.ArrayList;
import java.util.Iterator;

public class ClassSamples2 {

	public static void main(String[] args) {
		if(args.length > 0) {
		System.out.println(args[0]);
		}
		Object[] objects = new Object[1];
			System.out.println(objects.length);
			objects[0]= "Test";
			
			List<String> myList = new ArrayList<String>();
			myList.add("test");
			System.out.println(myList.size());
			
			/*Collection<Integer> col = new ArrayList<Integer>();
			col.add(12);
			col.add(11);
			col.add(2); */
			Iterator it = col.iterator();
			while (it.hasNext()) {
					System.out.println(it.next());
			}
			
			List<Integer> breakIt = new ArrayList<Integer>();
			for(int i = 0; i < 10; i++) {
				breakIt.add(i);
			}
			
	}

	} 
