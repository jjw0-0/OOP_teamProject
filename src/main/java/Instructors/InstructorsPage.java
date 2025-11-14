package Instructors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;


public class InstructorsPage extends JFrame{

    static Container container=null;

//    public void addComponentsToPane(){
//        Container pane=getContentPane();
//    }

    public void createAndShowGUI(){
        //전체 프레임
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(1000,600);
        container=this.getContentPane();
        container.setLayout(new BorderLayout());

        setupMainPanel();
        setupSidePanel();

        //addComponentsToPane();
        //pack();
        this.setResizable(false); // 화면 확대 금지
        setVisible(true);
    }
    JPanel setupMainPanel(){
        //메인 화면
        JPanel mainPanel=new JPanel();
        mainPanel.setPreferredSize(new Dimension(760,600));
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS)); // 수직정렬

        mainPanel.setBackground(Color.white); // 배경
        container.add(mainPanel,BorderLayout.CENTER);

        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(setupSubjectsPanel());

        mainPanel.add(Box.createVerticalStrut(30));
        JPanel wrapSearchBar=new RoundedPanel(13);
        wrapSearchBar.setMaximumSize(new Dimension(674,29));
        wrapSearchBar.setBackground(Color.white);
        wrapSearchBar.setLayout(new BoxLayout(wrapSearchBar,BoxLayout.X_AXIS)); // 수평
        wrapSearchBar.add(Box.createHorizontalStrut(405));
        wrapSearchBar.add(setupSearchBar());
        mainPanel.add(wrapSearchBar);

        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(setupInstructorsList());

        return mainPanel;

    }

    JPanel setupInstructorsList(){
        JPanel InstructorsList=new JPanel();
        InstructorsList.setMaximumSize(new Dimension(701,378));
        InstructorsList.setLayout(new GridLayout(2,4,10,10));
        InstructorsList.setBorder(new EmptyBorder(8,18,8,18));
        InstructorsList.setBackground(Color.white);

        for(int i=0;i<8;i++){
            InstructorsList.add(Instructor());
        }

        return InstructorsList;
    }

    JPanel Instructor(){
        JPanel InstructorCard=new RoundedPanel(18);
        InstructorCard.setMaximumSize(new Dimension(156,176));
        InstructorCard.setLayout(new BoxLayout(InstructorCard,BoxLayout.Y_AXIS)); // 수직정렬
        InstructorCard.setBackground(new Color(0xF5F5F5));

        InstructorData data=new InstructorData();

        InstructorCard.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 마우스 커서 설정
        InstructorCard.addMouseListener(new java.awt.event.MouseAdapter(){
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e){
                new InstructorDetailPopup(InstructorsPage.this,data).setVisible(true);
            }// 현재 인스턴스를 부모로 지정 후 팝업 열기
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e){ // hover 시 효과
                InstructorCard.setBackground(new Color(0xE5E5E5));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e){
                InstructorCard.setBackground(new Color(0xF5F5F5));
            }
        });

        //소개글
        InstructorCard.add(Box.createVerticalStrut(26));
        InstructorCard.add(introPanel(data));

        // 프로필
        JPanel Profile=new JPanel();
        Profile.setLayout(new BoxLayout(Profile,BoxLayout.X_AXIS));
        Profile.setMaximumSize(new Dimension(145,83));
        Profile.setOpaque(false);

        //사진
        Profile.add(createImage(data, 83,83));

        JPanel wrapInfo=new JPanel();
        wrapInfo.setLayout(new BoxLayout(wrapInfo,BoxLayout.Y_AXIS));
        wrapInfo.setMaximumSize(new Dimension(70,90));
        wrapInfo.setOpaque(false);

        //이름
        wrapInfo.add(Box.createVerticalStrut(14));
        wrapInfo.add(setupName(data));

        //별점
        wrapInfo.add(Box.createVerticalStrut(11));
        wrapInfo.add(createStar(data));

        Profile.add(wrapInfo);
        InstructorCard.add(Profile);

        return InstructorCard;
    }

    JPanel introPanel(InstructorData data){
        //소개글
        JPanel IntroductionPanel=new JPanel();
        IntroductionPanel.setMaximumSize(new Dimension(106,46));
        IntroductionPanel.setOpaque(false);

        JLabel Introduction=new JLabel(" "+data.Introduction);
        //Introduction.setForeground(Color.white);
        Introduction.setFont(new Font("맑은 고딕",Font.BOLD,15));

        IntroductionPanel.add(Introduction);

        return IntroductionPanel;
    }

    static JLabel createImage(InstructorData data, int width, int height){
        //사진
        ImageIcon imageIcon=data.getImage();
        Image scaledImage=imageIcon.getImage().getScaledInstance(width,height,Image.SCALE_SMOOTH);
        ImageIcon scaledImageIcon=new ImageIcon(scaledImage);

        return new JLabel(scaledImageIcon);
    }

    static JPanel setupName(InstructorData data){
        //이름
        JLabel name=new JLabel(data.name);
        name.setFont(new Font("맑은 고딕",Font.BOLD,18));

        JPanel namePanel=new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel,BoxLayout.X_AXIS));
        namePanel.setMaximumSize(new Dimension(54,19));
        namePanel.setOpaque(false);
        namePanel.add(name);

        return namePanel;
    }

    static JPanel createStar(InstructorData data){
        //별점
        JPanel starPanel=new JPanel();
        starPanel.setLayout(new BoxLayout(starPanel,BoxLayout.X_AXIS));
        starPanel.setMaximumSize(new Dimension(60,40));
        starPanel.setOpaque(false);
        //starPanel.setBackground(new Color(0xF5F5F5));

        JLabel star=new JLabel("<html><font color='#FFD700'>⭐</font>" +
                " <font color='black'>" + data.star + "</font></html>");
        star.setFont(new Font("Segoe UI Emoji",Font.BOLD,18));
        starPanel.add(star);

        return starPanel;
    }

    JPanel setupSearchBar(){
        JPanel searchPanel =new RoundedPanel(13);
        searchPanel.setLayout(new BorderLayout());
        searchPanel.setMaximumSize(new Dimension(300,29)); // 가로 원래는 295

        JTextField searchField=new JTextField();
        searchField.setMaximumSize(new Dimension(269,29));
        searchField.setBackground(Color.white);
        //searchField.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        searchPanel.add(searchField,BorderLayout.CENTER);

        JButton searchButton=new JButton("검색"); // 아이콘
        searchButton.setForeground(Color.white);
        searchButton.setMaximumSize(new Dimension(26,26));
        searchButton.setBackground(new Color(0x0C4A6E));
        searchButton.setFocusPainted(false);
        searchPanel.add(searchButton,BorderLayout.EAST);

        return searchPanel;
    }

    JPanel setupSubjectsPanel(){
        JPanel subjectsPanel=new JPanel();
        subjectsPanel.setLayout(new BoxLayout(subjectsPanel,BoxLayout.X_AXIS));//수평정렬
        subjectsPanel.setMaximumSize(new Dimension(674,57));
        subjectsPanel.setBackground(Color.white);

        String[] subjects={"국어", "수학", "영어", "사회", "과학", "한국사"};

        for(String subject:subjects){
            subjectsPanel.add(Box.createHorizontalStrut(15));
            subjectsPanel.add(createSubjectPanel(subject));
        }

        return subjectsPanel;
    }

    JPanel createSubjectPanel(String text){
        JLabel subjectName=new JLabel(text);
        subjectName.setFont(new Font("맑은 고딕", Font.BOLD,16));
        subjectName.setHorizontalAlignment(JLabel.CENTER); // 수평 가운데
        subjectName.setVerticalAlignment(JLabel.CENTER);; // 수직 가운데

        JPanel subjectPanel=new RoundedPanel(8,1,new Color(0x1E6EA0));
        subjectPanel.setLayout(new BorderLayout());
        subjectPanel.setMaximumSize(new Dimension(92,60));
        subjectPanel.setPreferredSize(new Dimension(92,60));
        subjectPanel.setBackground(Color.red);

        subjectPanel.add(subjectName); // 과목이름
        subjectPanel.setBackground(Color.white);

        return subjectPanel;

    }

    JPanel setupSidePanel(){
        //사이드바
        JPanel sidePanel=new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel,BoxLayout.Y_AXIS)); // 사이드바는 수직정렬 BoxLayout
        sidePanel.setPreferredSize(new Dimension(240,600));
        sidePanel.setBackground(new Color(0x0C4A6E)); // 배경

        sidePanel.add(Box.createVerticalStrut(27)); // 로고 위 여백 27
        sidePanel.add(setupLogo());
        sidePanel.add(Box.createVerticalStrut(40)); // 로고 아래 여백 40

        sidePanel.add(setupSideMenu());
        sidePanel.add(Box.createVerticalStrut(68)); // 사이드메뉴 아래 여백 68

        sidePanel.add(setupAuthMenu());

        container.add(sidePanel,BorderLayout.WEST);

        return sidePanel;
    }

    JPanel setupLogo(){
        JPanel logoPanel=new JPanel();
        logoPanel.setLayout(new BoxLayout(logoPanel,BoxLayout.X_AXIS)); // 로고 패널은 수평정렬 BoxLayout
        logoPanel.setMaximumSize(new Dimension(210,66));
        logoPanel.add(Box.createHorizontalStrut(17));
        logoPanel.setBackground(new Color(0x0C4A6E)); // 배경#0C4A6E

        JLabel logo=new JLabel("ILTAGANGSA");
        logo.setFont(new Font("Inknut Antiqua",Font.PLAIN,24));
        logo.setForeground(Color.white);

        logoPanel.add(logo);

        return logoPanel;
    }
    JPanel setupSideMenu(){
        JPanel menuPanel=new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel,BoxLayout.Y_AXIS)); // 메뉴 패널 수직정렬
        menuPanel.setMaximumSize(new Dimension(226,261));
        menuPanel.setPreferredSize(new Dimension(226,261));
        menuPanel.setBackground(new Color(0x0C4A6E));

        //menuPanel.setBorder(new EmptyBorder(0,0,0,0));
        menuPanel.add(createMenuItemPanel("홈"));
        menuPanel.add(Box.createVerticalStrut(9));
        menuPanel.add(createMenuItemPanel("강의"));
        menuPanel.add(Box.createVerticalStrut(9));
        menuPanel.add(createMenuItemPanel("강사"));
        menuPanel.add(Box.createVerticalStrut(9));
        menuPanel.add(createMenuItemPanel("마이페이지"));
        menuPanel.add(Box.createVerticalStrut(9));
        menuPanel.add(createMenuItemPanel("도움말"));

        return menuPanel;

    }
    JPanel createMenuItemPanel(String text){
        JPanel menuItemPanel=new JPanel();
        menuItemPanel.setLayout(new BorderLayout());
        menuItemPanel.setMaximumSize(new Dimension(240,45));
        menuItemPanel.setBackground(new Color(0x0C4A6E));//new Color(0x0C4A6E)
        menuItemPanel.setBorder(new EmptyBorder(0,0,0,0));
        menuItemPanel.add(Box.createVerticalStrut(9), BorderLayout.SOUTH);

        JLabel label=new JLabel(text);
        label.setFont(new Font("맑은 고딕",Font.BOLD,15));
        label.setForeground(Color.white);
        label.setBorder(new EmptyBorder(0,7,0,13));
        menuItemPanel.add(label,BorderLayout.WEST);

        return menuItemPanel;
    }
    // 로그인, 회원가입
    JPanel setupAuthMenu(){
        JPanel authMenu=new JPanel();
        authMenu.setLayout(new BoxLayout(authMenu,BoxLayout.Y_AXIS)); // 수직정렬
        authMenu.setMaximumSize(new Dimension(226,90));
        authMenu.setBackground(new Color(0x0C4A6E));

        authMenu.add(createMenuItemPanel("로그인"));
        authMenu.add(Box.createVerticalStrut(9));
        authMenu.add(createMenuItemPanel("회원가입"));

        return authMenu;
    }

    static JScrollPane createScrollPane(JPanel panel, int width, int height){
        JScrollPane scrollPane=new JScrollPane(panel);
        scrollPane.setMaximumSize(new Dimension(width, height));
        scrollPane.setPreferredSize(new Dimension(width, height));
        scrollPane.setBorder(null);
        //필요할 때만 스크롤
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        return scrollPane;
    }

    public static void main(String[] args) {
        InstructorsPage main=new InstructorsPage();
        main.createAndShowGUI();;
    }
}
