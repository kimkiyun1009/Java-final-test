package kky;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;

public class Gongji {

    private static final String ID_SAVE_FILE = "id_save.txt"; // ID 저장 파일 경로

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

            // ID 라벨 및 필드
            JLabel idLabel = new JLabel("ID (학번)");
            idLabel.setBounds(50, 80, 80, 25);
            idLabel.setFont(new Font("맑은 고딕", Font.BOLD, 12));
            mainPanel.add(idLabel);

            idField = new JTextField();
            idField.setBounds(110, 80, 185, 25);
            mainPanel.add(idField);

            // PW 라벨 및 필드
            JLabel pwLabel = new JLabel("PW");
            pwLabel.setBounds(50, 120, 80, 25);
            pwLabel.setFont(new Font("Arial", Font.BOLD, 12));
            mainPanel.add(pwLabel);

            passwordField = new JPasswordField();
            passwordField.setBounds(110, 120, 185, 25);
            mainPanel.add(passwordField);

            // 체크박스
            saveIdCheckBox = new JCheckBox("아이디 저장");
            saveIdCheckBox.setBounds(130, 160, 100, 25);
            saveIdCheckBox.setBackground(Color.WHITE);
            mainPanel.add(saveIdCheckBox);

            // 로그인 버튼 중앙 정렬
            loginButton = new JButton("로그인");
            loginButton.setSize(200, 30);
            loginButton.setBackground(Color.BLACK);
            loginButton.setForeground(Color.WHITE);
            loginButton.setFocusPainted(false);

            // 버튼 위치 계산 (중앙 정렬)
            int buttonX = (400 - loginButton.getWidth()) / 2;
            int buttonY = 200;
            loginButton.setLocation(buttonX, buttonY);

            mainPanel.add(loginButton);

            // ID 불러오기
            loadSavedId();

            // 로그인 버튼 클릭 이벤트
            loginButton.addActionListener((ActionEvent e) -> {
                saveIdIfNeeded(); // ID 저장
                dispose(); // 현재 창 닫기
                SwingUtilities.invokeLater(() -> {
                    GongjiApp gongjiApp = new GongjiApp();
                    gongjiApp.setVisible(true);
                });
            });
        }

        private void saveIdIfNeeded() {
            if (saveIdCheckBox.isSelected()) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(ID_SAVE_FILE))) {
                    writer.write(idField.getText());
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "ID 저장에 실패했습니다: " + e.getMessage());
                }
            } else {
                // 체크 해제된 경우 파일 삭제
                File file = new File(ID_SAVE_FILE);
                if (file.exists()) {
                    file.delete();
                }
            }
        }

        private void loadSavedId() {
            File file = new File(ID_SAVE_FILE);
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String savedId = reader.readLine();
                    if (savedId != null) {
                        idField.setText(savedId);
                        saveIdCheckBox.setSelected(true); // 저장 체크박스 선택 상태로 설정
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "ID 로드에 실패했습니다: " + e.getMessage());
                }
            }
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
            addItem("JAVA프로그래밍2", "전공수업", "https://example.com/1");
            addItem("GUI프로그래밍", "전공수업", "https://example.com/2");
            addItem("운영체제", "전공수업", "https://example.com/3");
            addItem("빅데이터", "전공수업", "https://example.com/4");
            addItem("알고리즘 설계", "전공수업", "https://example.com/5");
            addItem("국제개발협력", "교양수업", "https://example.com/6");
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
