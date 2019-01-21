
//u10616024 �i�|��
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class TestGame extends Application {
	TextField name, timer, text_number;
	RadioButton radio_number, radio_color;
	TabPane tab;
	Label answer_number, answer_color, timer_label;
	VBox record_number, record_color, record_all;
	ChoiceBox<String>[] choice_color = new ChoiceBox[4];
	Button enter_number, enter_color, again_number, again_color, save_button, load_button, start_button;
	int get_time; // ���o�ϥΪ̿�J���ɶ�
	int count = 0; // �p����
	int check = 0; // �p�⧹������
	int repeat = 0;// �����O�_������
	String status = ""; // ���o��������
	FileChooser fileChooser = new FileChooser();
	ArrayList<String> load_file = new ArrayList<>(); // �s��Ū�ɪ����e
	ArrayList<Integer> choice_ans = new ArrayList<>(); // �s��üƮɪ��Ʀr
	ArrayList<String> new_record = new ArrayList<>(); // �s��s������
	SecureRandom random = new SecureRandom();
	int[][] save_num = new int[2][4]; // �Ĥ@���s���T���� �ĤG���s�ϥΪ̼Ȯɪ�����
	Timer timer_downCount; // �p�ɾ�

	public static void main(String[] args) {
		launch(args);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("hw_game.fxml")); // ���Jfxml��

		Scene scene = new Scene(root, 560, 675); // ��Ū�i��FXML���@��Scene��root node
		primaryStage.setScene(scene);
		primaryStage.setTitle("�q�q��"); // ���Stage
		primaryStage.show();

		// �Nfxml�ɤ�������Pjava�ɤ������󰵳s��
		name = (TextField) scene.lookup("#name");
		timer = (TextField) scene.lookup("#timer");
		text_number = (TextField) scene.lookup("#text_number");
		radio_number = (RadioButton) scene.lookup("#radio_number");
		radio_color = (RadioButton) scene.lookup("#radio_color");
		tab = (TabPane) scene.lookup("#tab");
		answer_number = (Label) scene.lookup("#answer_number");
		answer_color = (Label) scene.lookup("#answer_color");
		timer_label = (Label) scene.lookup("#timer_label");
		record_number = (VBox) scene.lookup("#record_number");
		record_color = (VBox) scene.lookup("#record_color");
		record_all = (VBox) scene.lookup("#record_all");
		choice_color[0] = (ChoiceBox<String>) scene.lookup("#choice_color1");
		choice_color[1] = (ChoiceBox<String>) scene.lookup("#choice_color2");
		choice_color[2] = (ChoiceBox<String>) scene.lookup("#choice_color3");
		choice_color[3] = (ChoiceBox<String>) scene.lookup("#choice_color4");
		enter_number = (Button) scene.lookup("#enter_number");
		enter_color = (Button) scene.lookup("#enter_color");
		again_number = (Button) scene.lookup("#again_number");
		again_color = (Button) scene.lookup("#again_color");
		save_button = (Button) scene.lookup("#save_button");
		load_button = (Button) scene.lookup("#load_button");
		start_button = (Button) scene.lookup("#start_button");

		// �]�w�C��U�Կ�檺���e,�ιw�]���զ�
		choice_color[0].getItems().addAll("��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��");
		choice_color[0].getSelectionModel().select("��");
		choice_color[1].getItems().addAll("��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��");
		choice_color[1].getSelectionModel().select("��");
		choice_color[2].getItems().addAll("��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��");
		choice_color[2].getSelectionModel().select("��");
		choice_color[3].getItems().addAll("��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��");
		choice_color[3].getSelectionModel().select("��");

		// �]�w�C�ӫ��s�w�]���i���i��
		enter_color.setDisable(true);
		again_color.setDisable(true);
		enter_number.setDisable(true);
		again_number.setDisable(true);
		text_number.setDisable(true);
		choice_color[0].setDisable(true);
		choice_color[1].setDisable(true);
		choice_color[2].setDisable(true);
		choice_color[3].setDisable(true);
		timer_label.setVisible(false);
		radio_number.setSelected(true);
		tab.getTabs().get(1).setDisable(true);

		// ����U�q�Ʀr��,�۰ʤ�����Ʀr����,�B�C�⭶���������i��
		radio_number.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				radio_color.setSelected(false);
				tab.getTabs().get(1).setDisable(true);
				tab.getTabs().get(0).setDisable(false);
				tab.getSelectionModel().select(0);
			}

		});
		// ����U�q�C���,�۰ʤ������C�⭶��,�B�Ʀr�����������i��
		radio_color.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				radio_number.setSelected(false);
				tab.getTabs().get(0).setDisable(true);
				tab.getTabs().get(1).setDisable(false);
				tab.getSelectionModel().select(1);
			}
		});

		// ����U�}�l���î�
		start_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub

				// �T�{�m�W�P�p�ɪ���J�جO�_�����e
				if (!(timer.getText().isEmpty()) && !(name.getText().isEmpty())) {
					// �T�{�p�ɪ���J�جO�_��J���O�Ʀr
					if (isInteger(timer.getText())) {
						// �����C���,�N�C�⪺�U�Կ��}�Ҭ��i�H�ϥ�,�B�N0~11�[�Jchoice_ans��
						if (tab.getTabs().get(0).isDisable()) {
							choice_color[0].setDisable(false);
							choice_color[1].setDisable(false);
							choice_color[2].setDisable(false);
							choice_color[3].setDisable(false);
							enter_color.setDisable(false);
							status = "color";
							for (int a = 0; a < 11; a++) {
								choice_ans.add(a);
							}
						} else {
							// ���ܼƦr��,�N�Ʀr���U�Կ��}�Ҭ��i�H�ϥ�,�B�N0~9�[�Jchoice_ans��
							text_number.setDisable(false);
							enter_number.setDisable(false);
							status = "number";
							for (int a = 0; a < 10; a++) {
								choice_ans.add(a);
							}
						}
						// �H���qchoice_ans���o4�ӼƦr,��L���Y�R��,�H�K���o���ƪ��Ʀr
						for (int a = 0; a < 4; a++) {
							int tem = random.nextInt(choice_ans.size() - 1);
							save_num[0][a] = choice_ans.get(tem) + 1;
							choice_ans.remove(tem);
						}

						// ���եθ��,�ΥH���D���D�ѵ�
						/*
						 * System.out.print(save_num[0][0]);
						 * System.out.print(save_num[0][1]);
						 * System.out.print(save_num[0][2]);
						 * System.out.print(save_num[0][3]);
						 */

						// �N�C���e�����\�ॴ�},�B�N�W�誺��J�������,�קK�ϥΪ̶ç�y�����~
						again_color.setDisable(false);
						again_number.setDisable(false);
						timer.setStyle("-fx-border-color:#d4d4d1;");
						name.setStyle("-fx-border-color:#d4d4d1;");
						radio_number.setDisable(true);
						radio_color.setDisable(true);
						name.setDisable(true);
						timer.setVisible(false);
						timer_label.setVisible(true);
						get_time = Integer.valueOf(timer.getText());
						count = 0;
						start_button.setDisable(true);
						clock();
					} else {
						// ��p�ɾ���J���������,�N�p�ɾ����س]������,�����ϥΪ̦��B���~
						timer.setStyle("-fx-border-color:#E53A40;");
					}
				} else if (timer.getText().isEmpty() && name.getText().isEmpty()) {
					// ��p�ɾ��Ωm�W��쬰�ť�,�N��̪��س]������,�����ϥΪ̦��B���~
					timer.setStyle("-fx-border-color:#E53A40;");
					name.setStyle("-fx-border-color:#E53A40;");
				} else if (timer.getText().isEmpty()) {
					// ��p�ɾ���쬰�ť�,�N�س]������,�����ϥΪ̦��B���~
					timer.setStyle("-fx-border-color:#E53A40;");
					name.setStyle("-fx-border-color:#d4d4d1;");
				} else {
					// ��m�W��쬰�ť�,�N�س]������,�����ϥΪ̦��B���~
					name.setStyle("-fx-border-color:#E53A40;");
					timer.setStyle("-fx-border-color:#d4d4d1;");
				}
			}

		});
		// ���U�T�{�ɶ}�l�P�_
		enter_number.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				gameStart();
			}
		});
		// ���U�T�{�ɶ}�l�P�_
		enter_color.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				gameStart();
			}
		});
		// ���U�����ɦ^��@�}�l��J�m�W�ήɶ����e��
		again_color.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				restart();
			}
		});
		// ���U�����ɦ^��@�}�l��J�m�W�ήɶ����e��
		again_number.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				restart();
			}
		});
		// ���UŪ�ɫ�}�l����ɮרȩ̀�Ū��,�[�J�U�誺�����椤
		load_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				File selectedFile = fileChooser.showOpenDialog(null);
				try {
					// �ϥ�BufferedReader�@��@��i��Ū��,�ñN��ƥ[�iload_file��
					@SuppressWarnings("resource")
					BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
					String line;
					while ((line = reader.readLine()) != null) {
						load_file.add(line);
					}

				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// �Nload_file������Ƥ@�Ӥ@�Ӧs��label,�å[�J�U�誺vbox��
				for (int a = 0; a < load_file.size(); a++) {
					Label tem = new Label(load_file.get(a));
					tem.setStyle("-fx-font-size:18px;");
					tem.setMinHeight(30);
					record_all.getChildren().add(tem);
				}
			}
		});
		// ���U�s�����N�U������檺��Ʀs��txt��
		save_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				// �ϥ� FileChooser�Φۭq��SaveFile�i��s��
				FileChooser fileChooser = new FileChooser();
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
				fileChooser.getExtensionFilters().add(extFilter);

				File file = fileChooser.showSaveDialog(primaryStage);
				if (file != null) {
					SaveFile(load_file, file);
				}
			}
		});

	}

	// �P�_�O�_�����,�p�G�Ʀr�b�i���ഫ�ɵo�Ϳ��~�Y�����Ʀr,�^��false
	public static boolean isInteger(String value) {
		try {
			int a = Integer.parseInt(value);
			if (a > 0) {
				return true;
			} else {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
	}

	// �p��
	public void clock() {
		timer_downCount = new Timer();
		// �s�W�@��TimerTask
		TimerTask showtime = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						timer_label.setText(String.valueOf(get_time - count)); // ��X�ɶ�
						count++;
						// ���ƶW�L��J���ɶ��Y����,���Ƭ�-1,�ñN�����������[�ܬ����椤
						if (count > get_time) {
							check = -1;
							timer_downCount.cancel();
							if (tab.getTabs().get(0).isDisable()) {
								choice_color[0].setDisable(true);
								choice_color[1].setDisable(true);
								choice_color[2].setDisable(true);
								choice_color[3].setDisable(true);
								enter_color.setDisable(true);
							} else {
								text_number.setDisable(true);
								enter_number.setDisable(true);

							}
							addRecord();
						}
					}
				});

			}
		};
		// �ϥ�timer_downCount���p�ɾ�,�C��i��@��showtime ������
		timer_downCount.schedule(showtime, 0, 1000);
	}

	// ����U�T�{���,�Y�}�l�P�_�Xa�Xb
	public void gameStart() {
		repeat = 0;
		// ���ܪ��Ҧ����Ʀr��
		if (status.equals("number")) {
			// �T�{��J���Ʀr�O�_��4��,���O���ܷ|�ά��خش�����J���~
			if (isInteger(text_number.getText()) && text_number.getText().length() == 4) {
				// �T�{�Ʀr��������,�p�G���ƴN�]repeat = 1
				for (int a = 0; a < 3; a++) {
					String tem = String.valueOf(text_number.getText().charAt(a));
					for (int b = a + 1; b < 4; b++) {
						if (tem.equals(String.valueOf(text_number.getText().charAt(b)))) {
							repeat = 1;
							break;
						}
					}
					if (repeat == 1) {
						break;
					}
				}
				if (repeat == 0) {
					text_number.setStyle("-fx-border-color:#d4d4d1;");
					check++; // ���ƥ[1
					// �T�{�C�@�ӼƦr,�p�G�Ʀr�P���ת���m�ۦP�h�s�J2,�Y��m���P���Ʀr�ۦP�h�s1,���S���s0
					for (int a = 0; a < 4; a++) {
						int tem = Integer.valueOf(String.valueOf(text_number.getText().charAt(a)));
						if (tem == save_num[0][a]) {
							save_num[1][a] = 2;
						} else {
							int check = 0;
							for (int b = 0; b < 4; b++) {
								if (tem == save_num[0][b]) {
									save_num[1][a] = 1;
									check = 1;
								}
							}
							if (check == 0) {
								save_num[1][a] = 0;
							}
						}
					}
					// �p��C�@����2�M1������,���O��a�Mb
					int num_a = 0;
					int num_b = 0;
					for (int a = 0; a < 4; a++) {
						if (save_num[1][a] == 1)
							num_b++;
						else if (save_num[1][a] == 2)
							num_a++;
					}
					// �Y4a�h���\,�N�p�ɾ����U��,�N�O���[�i�����椤
					if (num_a == 4) {
						answer_number.setText("You're Right!");
						timer_downCount.cancel();
						Label tem = new Label(text_number.getText() + " : Correct!");
						tem.setStyle("-fx-font-size:18px;");
						tem.setMinHeight(30);
						record_number.getChildren().add(tem);
						text_number.setDisable(true);
						enter_number.setDisable(true);
						addRecord();
					} else {
						// ��X�Xa�Xb,�ñN�o���������[�i�����C�����L�{������
						answer_number.setText(num_a + "A" + num_b + "B");
						Label tem = new Label(text_number.getText() + " : " + answer_number.getText());
						tem.setStyle("-fx-font-size:18px;");
						tem.setMinHeight(30);
						record_number.getChildren().add(tem);
					}
				} else {
					// �p�G�Ʀr���ƫh���J�ا令����,�����ϥΪ̿�J���~
					text_number.setStyle("-fx-border-color:#E53A40;");
				}
			} else {
				text_number.setStyle("-fx-border-color:#E53A40;");
			}

		} else {
			// �T�{�Ʀr��������,�p�G���ƴN�]repeat = 1
			for (int a = 0; a < 3; a++) {
				String tem = choice_color[a].getSelectionModel().getSelectedItem();
				for (int b = a + 1; b < 4; b++) {
					if (tem.equals(choice_color[b].getSelectionModel().getSelectedItem())) {
						repeat = 1;
						break;
					}
				}
				if (repeat == 1) {
					break;
				}
			}
			if (repeat == 0) {
				choice_color[0].setStyle("-fx-border-color:#d4d4d1; -fx-font-size:18px;");
				choice_color[1].setStyle("-fx-border-color:#d4d4d1; -fx-font-size:18px;");
				choice_color[2].setStyle("-fx-border-color:#d4d4d1; -fx-font-size:18px;");
				choice_color[3].setStyle("-fx-border-color:#d4d4d1; -fx-font-size:18px;");
				check++; // ���ƥ[1
				// �@���C������@�ӼƦr,�T�{�C�@�����Ʀr,�p�G�Ʀr�P���ת���m�ۦP�h�s�J2,�Y��m���P���Ʀr�ۦP�h�s1,���S���s0
				for (int a = 0; a < 4; a++) {
					int tem = choice_color[a].getSelectionModel().getSelectedIndex() + 1;
					if (tem == save_num[0][a]) {
						save_num[1][a] = 2;
					} else {
						int check = 0;
						for (int b = 0; b < 4; b++) {
							if (tem == save_num[0][b]) {
								save_num[1][a] = 1;
								check = 1;
							}
						}
						if (check == 0) {
							save_num[1][a] = 0;
						}
					}
				}
				// �p��C�@����2�M1������,���O��a�Mb
				int num_a = 0;
				int num_b = 0;
				for (int a = 0; a < 4; a++) {
					if (save_num[1][a] == 1)
						num_b++;
					else if (save_num[1][a] == 2)
						num_a++;
				}
				// �Y4a�h���\,�N�p�ɾ����U��,�N�O���[�i�����椤
				if (num_a == 4) {
					answer_color.setText("You're Right!");
					timer_downCount.cancel();
					Label tem = new Label(choice_color[0].getSelectionModel().getSelectedItem()
							+ choice_color[1].getSelectionModel().getSelectedItem()
							+ choice_color[2].getSelectionModel().getSelectedItem()
							+ choice_color[3].getSelectionModel().getSelectedItem() + " : Correct!");
					tem.setStyle("-fx-font-size:18px;");
					tem.setMinHeight(30);
					record_color.getChildren().add(tem);
					text_number.setDisable(true);
					enter_number.setDisable(true);
					addRecord();
				} else {
					// ��X�Xa�Xb,�ñN�o���������[�i�����C�����L�{������
					answer_color.setText(num_a + "A" + num_b + "B");
					Label tem = new Label(choice_color[0].getSelectionModel().getSelectedItem()
							+ choice_color[1].getSelectionModel().getSelectedItem()
							+ choice_color[2].getSelectionModel().getSelectedItem()
							+ choice_color[3].getSelectionModel().getSelectedItem() + " : " + answer_color.getText());
					tem.setStyle("-fx-font-size:18px;");
					tem.setMinHeight(30);
					record_color.getChildren().add(tem);
				}
			} else {
				// �p�G�Ʀr���ƫh���J�ا令����,�����ϥΪ̿�J���~
				choice_color[0].setStyle("-fx-border-color:#E53A40; -fx-font-size:18px;");
				choice_color[1].setStyle("-fx-border-color:#E53A40; -fx-font-size:18px;");
				choice_color[2].setStyle("-fx-border-color:#E53A40; -fx-font-size:18px;");
				choice_color[3].setStyle("-fx-border-color:#E53A40; -fx-font-size:18px;");
			}
		}
	}

	// ������,�N�Ҧ����s�]�^�w�]���A
	public void restart() {
		check = 0;
		choice_color[0].getSelectionModel().select("��");
		choice_color[1].getSelectionModel().select("��");
		choice_color[2].getSelectionModel().select("��");
		choice_color[3].getSelectionModel().select("��");
		record_number.getChildren().clear();
		record_color.getChildren().clear();
		answer_number.setText("");
		answer_color.setText("");
		text_number.setText("");
		name.setDisable(false);
		radio_number.setDisable(false);
		radio_color.setDisable(false);
		name.clear();
		timer_label.setVisible(false);
		timer.setVisible(true);
		timer.clear();
		start_button.setDisable(false);
		choice_color[0].setDisable(true);
		choice_color[1].setDisable(true);
		choice_color[2].setDisable(true);
		choice_color[3].setDisable(true);
		enter_color.setDisable(true);
		text_number.setDisable(true);
		enter_number.setDisable(true);
		choice_ans.clear();
		for (int a = 0; a < 4; a++) {
			save_num[1][a] = 0;
		}
	}

	// �g��
	public void SaveFile(ArrayList<String> content, File file) {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(file);
			// �NŪ�ɪ���Ʀs�J
			for (int a = 0; a < load_file.size(); a++) {
				fileWriter.write(load_file.get(a));
				fileWriter.write(System.getProperty("line.separator")); // ����
			}
			// �N�{�����s��Ʀs�J
			for (int a = 0; a < new_record.size(); a++) {
				fileWriter.write(new_record.get(a));
				fileWriter.write(System.getProperty("line.separator"));// ����
			}
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// �[�J������
	public void addRecord() {
		String[] answer = new String[4];
		// �̪����Ҧ����o�����C��������
		if (status.equals("number")) {
			for (int a = 0; a < 4; a++) {
				answer[a] = String.valueOf(save_num[0][a]);
			}
		} else {
			for (int a = 0; a < 4; a++) {
				answer[a] = choice_color[0].getItems().get(save_num[0][a] - 1);
			}
		}
		// �N�������s��label,�å[�J�U�誺�����椤
		String tem_label = "�m�W : " + name.getText() + "   ���� : " + answer[0] + answer[1] + answer[2] + answer[3]
				+ "   ���� : " + check;
		new_record.add(tem_label);
		Label tem = new Label(tem_label);
		tem.setStyle("-fx-font-size:18px;");
		tem.setMinHeight(30);
		record_all.getChildren().add(tem);
	}
}