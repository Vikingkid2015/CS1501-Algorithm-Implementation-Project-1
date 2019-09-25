public class dlb_test
{
    public static void main(String[] args)
    {
        DLB test = new DLB();

        test.insert("tool");
        test.insert("test");
        
        System.out.println("Contains test: " + test.contains("test"));

        test.insert("tests");
        test.insert("asdf");
        test.insert("tea");
        test.insert("table");
        test.insert("tax");
        test.insert("able");

        System.out.println("Contains tests: " + test.contains("tests"));
        System.out.println("Contains asdf: " + test.contains("asdf"));
        System.out.println("Contains tea: " + test.contains("tea"));
        System.out.println("Contains tes: " + test.contains("tes"));

        System.out.println("Predctions for t: " + test.predict("t"));
    }
}
