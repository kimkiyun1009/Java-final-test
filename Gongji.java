package kky;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;

public class Gongji {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginGUI loginGUI = new LoginGUI();
            loginGUI.setVisible(true);
        });
    }

    // 로그인 GUI 클래스
    static class LoginGUI extends JFrame {
        private JTextField idField;
        private JPasswordField passwordField;
        private JCheckBox saveIdCheckBox;
        private JButton loginButton;

        public LoginGUI() {
            setTitle("로그인");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(400, 300);
            setLocationRelativeTo(null);
            setResizable(false);

            // 메인 패널
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(null);
            mainPanel.setBackground(Color.WHITE);
            add(mainPanel);

            // 타이틀 라벨
            JLabel titleLabel = new JLabel("로그인", JLabel.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
            titleLabel.setBounds(0, 20, 400, 30);
            mainPanel.add(titleLabel);

            // ID 라벨 및 필드
            JLabel idLabel = new JLabel("ID");
            idLabel.setBounds(50, 80, 80, 25);
            idLabel.setFont(new Font("Arial", Font.BOLD, 12));
            mainPanel.add(idLabel);

            idField = new JTextField();
            idField.setBounds(130, 80, 200, 25);
            mainPanel.add(idField);

            // PW 라벨 및 필드
            JLabel pwLabel = new JLabel("PW");
            pwLabel.setBounds(50, 120, 80, 25);
            pwLabel.setFont(new Font("Arial", Font.BOLD, 12));
            mainPanel.add(pwLabel);

            passwordField = new JPasswordField();
            passwordField.setBounds(130, 120, 200, 25);
            mainPanel.add(passwordField);

            // 체크박스
            saveIdCheckBox = new JCheckBox("아이디 저장");
            saveIdCheckBox.setBounds(130, 160, 100, 25);
            saveIdCheckBox.setBackground(Color.WHITE);
            mainPanel.add(saveIdCheckBox);

            // 로그인 버튼
            loginButton = new JButton("로그인");
            loginButton.setBounds(130, 200, 200, 30);
            loginButton.setBackground(Color.BLACK);
            loginButton.setForeground(Color.WHITE);
            loginButton.setFocusPainted(false);
            mainPanel.add(loginButton);

            // 로그인 버튼 클릭 이벤트
            loginButton.addActionListener((ActionEvent e) -> {
                dispose(); // 현재 창 닫기
                SwingUtilities.invokeLater(() -> {
                    GongjiApp gongjiApp = new GongjiApp();
                    gongjiApp.setVisible(true);
                });
            });
        }
    }

    // 공지사항 GUI 클래스
    static class GongjiApp extends JFrame {
        private JPanel contentPane;
        private JPanel listPanel;
        private ArrayList<ItemPanel> itemPanels;

        public GongjiApp() {
            setTitle("공지사항 어플");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(400, 300);
            setLocationRelativeTo(null);

            contentPane = new JPanel();
            contentPane.setLayout(new BorderLayout());
            setContentPane(contentPane);

            // 스크롤 가능한 리스트 패널
            listPanel = new JPanel();
            listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
            JScrollPane scrollPane = new JScrollPane(listPanel);
            contentPane.add(scrollPane, BorderLayout.CENTER);

            // 항목 추가
            itemPanels = new ArrayList<>();
            addItem("JAVA프로그래밍2", "전공수업", "https://hive.cju.ac.kr/usr/classroom/main.do?currentMenuId=&courseApplySeq=6405449&courseActiveSeq=95929");
            addItem("GUI프로그래밍", "전공수업", "https://hive.cju.ac.kr/usr/classroom/main.do?currentMenuId=&courseApplySeq=6405245&courseActiveSeq=95923");
            addItem("운영체제", "전공수업", "https://hive.cju.ac.kr/usr/classroom/main.do?currentMenuId=&courseApplySeq=6404939&courseActiveSeq=95897");
            addItem("빅데이터", "전공수업", "https://hive.cju.ac.kr/usr/classroom/main.do?currentMenuId=&courseApplySeq=6405363&courseActiveSeq=95925");
            addItem("알고리즘 설계", "전공수업", "https://hive.cju.ac.kr/usr/classroom/main.do?currentMenuId=&courseApplySeq=6405121&courseActiveSeq=95915");
            addItem("국제개발협력", "교양수업", "https://hive.cju.ac.kr/usr/classroom/main.do?currentMenuId=&courseApplySeq=6428863&courseActiveSeq=97419");
        }

        private void addItem(String subject, String title, String url) {
            ItemPanel item = new ItemPanel(subject, title, url);
            itemPanels.add(item);
            listPanel.add(item);
            refreshList();
        }

        private void refreshList() {
            listPanel.removeAll();
            Collections.sort(itemPanels, (a, b) -> Boolean.compare(b.isFavorite(), a.isFavorite()));

            for (ItemPanel item : itemPanels) {
                listPanel.add(item);
            }

            listPanel.revalidate();
            listPanel.repaint();
        }

        private class ItemPanel extends JPanel {
            private JLabel subjectLabel;
            private JLabel titleLabel;
            private StarButton starButton;
            private boolean favorite;
            private String url;
            private JLabel pinLabel; // 고정핀 이모티콘 라벨

            public ItemPanel(String subject, String title, String url) {
                this.url = url;
                setLayout(new BorderLayout());
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ));
                setBackground(Color.WHITE);

                // 고정핀 이모티콘 라벨 추가
                pinLabel = new JLabel("📌");
                pinLabel.setVisible(false); // 기본적으로 숨김
                add(pinLabel, BorderLayout.WEST);

                subjectLabel = new JLabel(subject);
                titleLabel = new JLabel(" - " + title);

                JPanel textPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                textPanel.setOpaque(false);
                textPanel.add(subjectLabel);
                textPanel.add(titleLabel);

                // 별표 버튼
                starButton = new StarButton();
                starButton.addActionListener(e -> toggleFavorite());

                this.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        openLink();
                    }
                });

                add(textPanel, BorderLayout.CENTER);
                add(starButton, BorderLayout.EAST);
            }

            private void toggleFavorite() {
                favorite = !favorite;

                // 고정핀 이모티콘 표시/숨김
                pinLabel.setVisible(favorite);

                starButton.setFavorite(favorite);
                refreshList();
            }

            private void openLink() {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "링크를 열 수 없습니다: " + ex.getMessage());
                }
            }

            public boolean isFavorite() {
                return favorite;
            }
        }

        private class StarButton extends JButton {
            private boolean favorite;

            public StarButton() {
                setPreferredSize(new Dimension(30, 30));
                setFocusPainted(false);
                setContentAreaFilled(false);
                setBorderPainted(false);
            }

            public void setFavorite(boolean favorite) {
                this.favorite = favorite;
                repaint();
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 별 그리기
                int[] xPoints = {15, 19, 29, 21, 24, 15, 6, 9, 1, 11};
                int[] yPoints = {1, 11, 11, 17, 27, 21, 27, 17, 11, 11};
                Polygon star = new Polygon(xPoints, yPoints, xPoints.length);

                g2.setColor(getParent().getBackground());
                g2.fillRect(0, 0, getWidth(), getHeight());

                if (favorite) {
                    g2.setColor(Color.YELLOW);
                    g2.fillPolygon(star);
                    g2.setColor(Color.BLACK);
                    g2.setStroke(new BasicStroke(2.5f));
                } else {
                    g2.setColor(Color.LIGHT_GRAY);
                    g2.fillPolygon(star);
                    g2.setColor(Color.BLACK);
                    g2.setStroke(new BasicStroke(1.0f));
                }
                g2.drawPolygon(star);
            }
        }
    }
}

