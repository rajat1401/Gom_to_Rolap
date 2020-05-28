package com.sqlsamples;


import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

//import cassandraApplication.WelcomeCassandra;

public class OptionsMenu extends JFrame {

    private JPanel contentPane;

    private JButton btnCreate;
    private JButton btnModify;
    private JComboBox comboBox;
    private JButton btnViewInfomation;
    private String info_selected;
    private JButton btnBack;
    private JButton btnProceedToMake;
    private JTextField txtProject;
    /**
     * Launch the application.
     */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					OptionsMenu frame = new OptionsMenu("p1","old");
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

    /**
     * Create the frame.
     */
    public OptionsMenu(String projectId,String userType) {

        this.setTitle("Options Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(500, 100, 582, 414);
        contentPane = new JPanel();
        contentPane.setBackground(Color.DARK_GRAY);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        btnCreate= new JButton("Create new Data Object");
        btnCreate.setFont(new Font("Dialog", Font.BOLD, 13));
        btnCreate.setBounds(119, 88, 219, 25);
        contentPane.add(btnCreate);

        btnModify = new JButton("Modify Data Object");
        btnModify.setFont(new Font("Dialog", Font.BOLD, 13));
        btnModify.setBounds(120, 129, 219, 25);
        contentPane.add(btnModify);

        JLabel lblProject = new JLabel("Project Name ");
        lblProject.setForeground(Color.YELLOW);
        lblProject.setHorizontalAlignment(SwingConstants.CENTER);
        lblProject.setFont(new Font("Times New Roman", Font.BOLD, 16));
        lblProject.setBounds(119, 47, 134, 23);
        contentPane.add(lblProject);


        String p_Info[]=fetchInfoFromDataBase(projectId);//gives a list of data objects or so to say.
        if(p_Info==null) {
            p_Info=new String[1];
            p_Info[1]="";
        }


        comboBox = new JComboBox(p_Info);
        comboBox.setBounds(350, 177, 86, 22);
        comboBox.setSelectedItem(null);
        contentPane.add(comboBox);

        btnViewInfomation = new JButton("View Data Object");
        btnViewInfomation.setFont(new Font("Dialog", Font.BOLD, 13));
        btnViewInfomation.setBounds(119, 176, 219, 25);
        contentPane.add(btnViewInfomation);

        btnBack = new JButton("Back");

        btnBack.setFont(new Font("Dialog", Font.BOLD, 13));
        btnBack.setBounds(212, 294, 97, 25);
        contentPane.add(btnBack);

        btnProceedToMake = new JButton("Proceed to ROLAP Schema..");
        btnProceedToMake.setFont(new Font("Dialog", Font.BOLD, 13));
        btnProceedToMake.setBounds(119, 228, 302, 25);
        contentPane.add(btnProceedToMake);

        txtProject = new JTextField();
        txtProject.setFont(new Font("Tahoma", Font.BOLD, 13));
        txtProject.setBackground(Color.WHITE);
        txtProject.setForeground(Color.BLACK);
        txtProject.setHorizontalAlignment(SwingConstants.CENTER);
        txtProject.setBounds(265, 47, 116, 22);
        contentPane.add(txtProject);
        txtProject.setColumns(10);
        txtProject.setText(projectId.toUpperCase());
        txtProject.setEditable(false);


        if(userType.equalsIgnoreCase("new"))
        {
            btnModify.setEnabled(false);
            btnViewInfomation.setEnabled(false);
            btnProceedToMake.setEnabled(false);
        }


        addListeners(projectId,userType);
    }

    public void addListeners(String projectId,String userType)
    {
        btnCreate.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                CreateNewInfo cr =new CreateNewInfo(projectId);
                cr.setVisible(true);
                dispose();
            }
        });

        btnModify.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                modifyInformation mI=new modifyInformation(projectId);
                mI.setVisible(true);
                dispose();
            }
        });


        comboBox.addItemListener(new ItemListener() {


            @Override
            public void itemStateChanged(ItemEvent e) {
                // TODO Auto-generated method stub

                if(e.getStateChange()==ItemEvent.SELECTED)
                {
                    info_selected=comboBox.getSelectedItem().toString().toLowerCase();

                }
                //				if(e.getSource()==Info_comboBox)
                //				{
                //					info_selected=Info_comboBox.getSelectedItem().toString().toLowerCase();
                //					getData(info_selected);
                //				}
                else
                {
                    JOptionPane.showMessageDialog(contentPane, "Kindly Choose one option");
                }

            }
        });
        btnViewInfomation.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                if(info_selected!=null &&!info_selected.trim().isEmpty())
                {
                    com.sqlsamples.DatabaseConnection dbcon=new com.sqlsamples.DatabaseConnection();
                    Connection con=dbcon.getConnection(projectId);
                    dbQueries dbQ=new dbQueries(con);
                    try {
                        RequirementsClass rc=dbQ.getInfo(info_selected);
                        confirmUserInput cu=new confirmUserInput(rc,"view");
                        cu.setVisible(true);
                        dispose();
                    } catch (SQLException e1) {
                        // TODO Auto-generated catch block
                        //e1.printStackTrace();
                        JOptionPane.showMessageDialog(contentPane, "Error in fetching Data Object", "Error Occured", 2);
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(contentPane, "Please select Data Object", "No Data Object selected", 1);
                }

            }
        });

        btnProceedToMake.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub

                //JOptionPane.showConfirmDialog(contentPane, "Are you sure you want ", title, optionType)
                btnCreate.setEnabled(false);
                btnModify.setEnabled(false);
                btnViewInfomation.setEnabled(false);
//				WelcomeCassandra ws=new WelcomeCassandra(projectId,userType);
                HashMap<String, HashMap<String, String>> papaAttributes= new HashMap<>();
                HashMap<String, HashMap<String, HashMap<String, String>>> papacat_attribute= new HashMap<>();
                String[] p_info= fetchInfoFromDataBase(projectId);
                com.sqlsamples.DatabaseConnection dbcon=new com.sqlsamples.DatabaseConnection();
                Connection con=dbcon.getConnection(projectId);
                dbQueries dbQ=new dbQueries(con);
                for (int i=0; i<p_info.length; i++){
                    try {
                        RequirementsClass rc=dbQ.getInfo(p_info[i]);
                        papaAttributes.put(p_info[i], rc.getAttributes());
//                        System.out.println("Data Object: " + p_info[i]);
//                        System.out.println("Aggregates: " + rc.getAggregateInfo());
                        papacat_attribute.put(p_info[i], rc.getCategory_attribute());
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }

                /*
                CONVERSION-ALGORITHM GOM4DW TO ROLAP
                 */
                Facts[] facts= new Facts[papaAttributes.size()];
                int count= 0;
                for (String key: papaAttributes.keySet()){//create the facts for each data object(unique)
                    facts[count]= new Facts(key);
                    facts[count].setAttributes(papaAttributes.get(key));//add attributes
                    count+= 1;
                }
                HashMap<String, Dimension> dimlist= new HashMap<>();//track
                HashMap<String, Dimension> subdimlist= new HashMap<>();//track
                for (String key: papacat_attribute.keySet()){
                    try {
                        RequirementsClass rc = dbQ.getInfo(key);
                        HashMap<String, ArrayList<String>> subcats = rc.getCategory_subcategory();
                        for (String cat: papacat_attribute.get(key).keySet()) {
                            if (!subcats.containsKey(cat)) {//for each category not seen before
                                if (!dimlist.containsKey(cat)) {
                                    Dimension d = new Dimension(cat);//create dimension
                                    d.setAttributes(papacat_attribute.get(key).get(cat));
                                    for (int i = 0; i < facts.length; i++) {//link D to F
                                        if (facts[i].name.equalsIgnoreCase(key)) {
                                            d.addFact(facts[i]);
                                            break;
                                        }
                                    }
                                    dimlist.put(cat, d);
                                }else{
                                    for(String d: dimlist.keySet()){
                                        if(d.equalsIgnoreCase(cat)){
                                            for (int i = 0; i < facts.length; i++) {
                                                if (facts[i].name.equalsIgnoreCase(key)) {
                                                    dimlist.get(d).addFact(facts[i]);
                                                    break;
                                                }
                                            }
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        for (String cat: papacat_attribute.get(key).keySet()) {
                            if (subcats.containsKey(cat)) {//for each category not seen before in SD
                                Dimension subd = new Dimension(cat);
                                if (!subdimlist.containsKey(cat)) {
                                    subd.setAttributes(papacat_attribute.get(key).get(cat));
                                    for (String dim : subcats.get(cat)) {
                                        dimlist.get(dim).addsubdim(subd);
                                    }
                                    subdimlist.put(cat, subd);
                                }else{//link SD to existing D
                                    for (String dim : subcats.get(cat)) {
                                        dimlist.get(dim).addsubdim(subd);
                                    }
                                }
                            }
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                //write ROLAP schema to a custom-formatted file as output (txt)
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
                    for (int i=0; i<facts.length; i++){
                        writer.write(facts[i].toString());
                    }
                    writer.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                dispose();
                Frame frame[]=Frame.getFrames();
                if(frame!=null)
                {
                    for(Frame f:frame)
                    {
                        f.dispose();
                    }
                }
                App ws=new App();
                ws.setVisible(true);
                //wc.main(new String [1]);

            }
        });
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                dispose();
                Frame f[]=getFrames();
                for(Frame s:f)
                {
                    System.out.println(s.getTitle());
                    if(s.getTitle().equalsIgnoreCase("Login Screen"))
                    {
                        s.setVisible(true);
                    }
                    else if (s.getTitle().equalsIgnoreCase("Welcome to the Tool"))
                    {
                        s.setVisible(true);
                    }
                }
            }
        });
    }

    public String [] fetchInfoFromDataBase(String projectID)
    {
        com.sqlsamples.DatabaseConnection dbcon=new com.sqlsamples.DatabaseConnection();
        Connection conn=dbcon.getConnection(projectID);
        dbQueries dbq=new dbQueries(conn);
        return(dbq.fetch_pInfo_FromDataBase(projectID));
    }
}
