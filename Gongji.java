package kky;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        private boolean deleteMode;

        public GongjiApp() {
            setTitle("공지사항 어플");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(400, 300);
            setLocationRelativeTo(null);

            contentPane = new JPanel();
            contentPane.setLayout(new BorderLayout());
            setContentPane(contentPane);

            // 상단 패널에 + 버튼 및 휴지통 버튼 추가
            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton addButton = new JButton("+");
            JButton deleteButton = new JButton("X"); // 휴지통 버튼을 X로 설정
            deleteButton.setForeground(Color.RED); // X 글자만 빨간색으로 설정
            deleteButton.setBorderPainted(false);
            deleteButton.setFocusPainted(false);
            deleteButton.setContentAreaFilled(false);
            topPanel.add(addButton);
            topPanel.add(deleteButton);
            contentPane.add(topPanel, BorderLayout.NORTH);

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

            // + 버튼 클릭 이벤트
            addButton.addActionListener(e -> showCreateCellDialog());

            // 휴지통 버튼 클릭 이벤트
            deleteButton.addActionListener(e -> toggleDeleteMode());
        }

        private void showCreateCellDialog() {
            String cellName = JOptionPane.showInputDialog(this, "이름을 입력하세요:", "새 셀 추가", JOptionPane.PLAIN_MESSAGE);
            if (cellName != null && !cellName.trim().isEmpty()) {
                addItem(cellName, "사용자 추가", "https://example.com/custom");
            }
        }

        private void toggleDeleteMode() {
            deleteMode = !deleteMode;
            for (ItemPanel item : itemPanels) {
                item.setDeleteMode(deleteMode);
            }
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
            private JButton starButton;
            private boolean favorite;
            private String url;
            private JLabel pinLabel; // 고정핀 이모티콘 라벨
            private JButton deleteButton; // 삭제 버튼

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

                // 1. 커서 스타일 변경
                subjectLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                titleLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

                // 5. 툴팁 추가
                subjectLabel.setToolTipText("이 글씨를 클릭하면 해당 링크로 이동합니다.");
                titleLabel.setToolTipText("이 글씨를 클릭하면 해당 링크로 이동합니다.");

                JPanel textPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                textPanel.setOpaque(false);
                textPanel.add(subjectLabel);
                textPanel.add(titleLabel);

                // 별표 버튼
                starButton = new JButton("⭐");
                starButton.addActionListener(e -> toggleFavorite());

                // 삭제 버튼
                deleteButton = new JButton("X");
                deleteButton.setPreferredSize(new Dimension(20, 20));
                deleteButton.setForeground(Color.RED); // X 글자만 빨간색으로 설정
                deleteButton.setBorderPainted(false);
                deleteButton.setFocusPainted(false);
                deleteButton.setContentAreaFilled(false);
                deleteButton.setVisible(false); // 기본적으로 숨김
                deleteButton.addActionListener(e -> confirmDelete());

                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
                buttonPanel.setOpaque(false);
                buttonPanel.add(starButton);
                buttonPanel.add(deleteButton);

                this.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (!deleteMode) {
                            openLink();
                        }
                    }
                });

                add(textPanel, BorderLayout.CENTER);
                add(buttonPanel, BorderLayout.EAST);
            }

            private void toggleFavorite() {
                favorite = !favorite;

                // 고정핀 이모티콘 표시/숨김
                pinLabel.setVisible(favorite);

                starButton.setText(favorite ? "🌟" : "⭐");
                refreshList();
            }

            private void openLink() {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "링크를 열 수 없습니다: " + ex.getMessage());
                }
            }

            private void confirmDelete() {
                int result = JOptionPane.showConfirmDialog(this, "삭제하시겠습니까?", "삭제 확인", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    itemPanels.remove(this);
                    listPanel.remove(this);
                    refreshList();
                }
            }

            public void setDeleteMode(boolean deleteMode) {
                deleteButton.setVisible(deleteMode);
            }

            public boolean isFavorite() {
                return favorite;
            }
        }
    }
}
