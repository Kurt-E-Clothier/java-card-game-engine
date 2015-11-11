 import javax.swing.*;
    import java.io.*;
    /* Author : Group 5
     * Test file for GUI to read filenames associated with rules file
     */
    public class FileName extends JFrame{
        JPanel pnl=new JPanel();
        public static void main (String[]args) {
            FileName print=new FileName();
            }
        JList list;
        

        @SuppressWarnings("unchecked")
        public FileName() {
            super("Swing Window");
            setSize(250,300);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setVisible(true);
            add(pnl);

            String path="/";
            File folder=new File(path);
            File[]listOfFiles = new File(System.getProperty("user.dir")+"/plugin").listFiles(new TextFileFilter());
            list=new JList(listOfFiles);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list.setLayoutOrientation(JList.VERTICAL);
            pnl.add(list);
            pnl.revalidate();
            }
    }
    
 

    class TextFileFilter implements FileFilter {
        public boolean accept(File file) {
            String name=file.getName();
            return name.length()<28&&name.contains("rule");
            }
        }
    
    
    