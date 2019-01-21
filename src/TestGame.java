
//u10616024 張育甄
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
	int get_time; // 取得使用者輸入的時間
	int count = 0; // 計算秒數
	int check = 0; // 計算完成次數
	int repeat = 0;// 紀錄是否有重複
	String status = ""; // 取得玩的種類
	FileChooser fileChooser = new FileChooser();
	ArrayList<String> load_file = new ArrayList<>(); // 存放讀檔的內容
	ArrayList<Integer> choice_ans = new ArrayList<>(); // 存放亂數時的數字
	ArrayList<String> new_record = new ArrayList<>(); // 存放新的紀錄
	SecureRandom random = new SecureRandom();
	int[][] save_num = new int[2][4]; // 第一項存正確答案 第二項存使用者暫時的答案
	Timer timer_downCount; // 計時器

	public static void main(String[] args) {
		launch(args);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("hw_game.fxml")); // 載入fxml檔

		Scene scene = new Scene(root, 560, 675); // 用讀進來FXML的作為Scene的root node
		primaryStage.setScene(scene);
		primaryStage.setTitle("猜猜樂"); // 顯示Stage
		primaryStage.show();

		// 將fxml檔中的物件與java檔中的物件做連結
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

		// 設定顏色下拉選單的內容,及預設為白色
		choice_color[0].getItems().addAll("白", "黑", "紅", "橘", "黃", "綠", "藍", "紫", "灰", "粉", "金", "銀");
		choice_color[0].getSelectionModel().select("白");
		choice_color[1].getItems().addAll("白", "黑", "紅", "橘", "黃", "綠", "藍", "紫", "灰", "粉", "金", "銀");
		choice_color[1].getSelectionModel().select("白");
		choice_color[2].getItems().addAll("白", "黑", "紅", "橘", "黃", "綠", "藍", "紫", "灰", "粉", "金", "銀");
		choice_color[2].getSelectionModel().select("白");
		choice_color[3].getItems().addAll("白", "黑", "紅", "橘", "黃", "綠", "藍", "紫", "灰", "粉", "金", "銀");
		choice_color[3].getSelectionModel().select("白");

		// 設定每個按鈕預設為可不可用
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

		// 當按下猜數字時,自動切換到數字頁面,且顏色頁面關閉不可按
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
		// 當按下猜顏色時,自動切換到顏色頁面,且數字頁面關閉不可按
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

		// 當按下開始按紐時
		start_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub

				// 確認姓名與計時的輸入框是否有內容
				if (!(timer.getText().isEmpty()) && !(name.getText().isEmpty())) {
					// 確認計時的輸入框是否填入的是數字
					if (isInteger(timer.getText())) {
						// 當選擇顏色時,將顏色的下拉選單開啟為可以使用,且將0~11加入choice_ans中
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
							// 當選擇數字時,將數字的下拉選單開啟為可以使用,且將0~9加入choice_ans中
							text_number.setDisable(false);
							enter_number.setDisable(false);
							status = "number";
							for (int a = 0; a < 10; a++) {
								choice_ans.add(a);
							}
						}
						// 隨機從choice_ans取得4個數字,選過的即刪除,以免取得重複的數字
						for (int a = 0; a < 4; a++) {
							int tem = random.nextInt(choice_ans.size() - 1);
							save_num[0][a] = choice_ans.get(tem) + 1;
							choice_ans.remove(tem);
						}

						// 測試用資料,用以知道此題解答
						/*
						 * System.out.print(save_num[0][0]);
						 * System.out.print(save_num[0][1]);
						 * System.out.print(save_num[0][2]);
						 * System.out.print(save_num[0][3]);
						 */

						// 將遊戲畫面的功能打開,且將上方的輸入欄位關閉,避免使用者亂改造成錯誤
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
						// 當計時器輸入不為正整數,將計時器的框設為紅色,提醒使用者此處錯誤
						timer.setStyle("-fx-border-color:#E53A40;");
					}
				} else if (timer.getText().isEmpty() && name.getText().isEmpty()) {
					// 當計時器及姓名欄位為空白,將兩者的框設為紅色,提醒使用者此處錯誤
					timer.setStyle("-fx-border-color:#E53A40;");
					name.setStyle("-fx-border-color:#E53A40;");
				} else if (timer.getText().isEmpty()) {
					// 當計時器欄位為空白,將框設為紅色,提醒使用者此處錯誤
					timer.setStyle("-fx-border-color:#E53A40;");
					name.setStyle("-fx-border-color:#d4d4d1;");
				} else {
					// 當姓名欄位為空白,將框設為紅色,提醒使用者此處錯誤
					name.setStyle("-fx-border-color:#E53A40;");
					timer.setStyle("-fx-border-color:#d4d4d1;");
				}
			}

		});
		// 按下確認時開始判斷
		enter_number.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				gameStart();
			}
		});
		// 按下確認時開始判斷
		enter_color.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				gameStart();
			}
		});
		// 按下重玩時回到一開始輸入姓名及時間的畫面
		again_color.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				restart();
			}
		});
		// 按下重玩時回到一開始輸入姓名及時間的畫面
		again_number.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				restart();
			}
		});
		// 按下讀檔後開始選取檔案並依序讀取,加入下方的紀錄欄中
		load_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				File selectedFile = fileChooser.showOpenDialog(null);
				try {
					// 使用BufferedReader一行一行進行讀取,並將資料加進load_file中
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
				// 將load_file中的資料一個一個存成label,並加入下方的vbox中
				for (int a = 0; a < load_file.size(); a++) {
					Label tem = new Label(load_file.get(a));
					tem.setStyle("-fx-font-size:18px;");
					tem.setMinHeight(30);
					record_all.getChildren().add(tem);
				}
			}
		});
		// 按下存檔鍵後將下方紀錄欄的資料存為txt檔
		save_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				// 使用 FileChooser及自訂的SaveFile進行存檔
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

	// 判斷是否為整數,如果數字在進行轉換時發生錯誤即不為數字,回傳false
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

	// 計時
	public void clock() {
		timer_downCount = new Timer();
		// 新增一個TimerTask
		TimerTask showtime = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						timer_label.setText(String.valueOf(get_time - count)); // 輸出時間
						count++;
						// 當秒數超過輸入的時間即停止,次數為-1,並將此局的紀錄加至紀錄欄中
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
		// 使用timer_downCount的計時器,每秒進行一次showtime 的任務
		timer_downCount.schedule(showtime, 0, 1000);
	}

	// 當按下確認鍵後,即開始判斷幾a幾b
	public void gameStart() {
		repeat = 0;
		// 當選擇的模式為數字時
		if (status.equals("number")) {
			// 確認輸入的數字是否為4個,不是的話會用紅框框提醒輸入錯誤
			if (isInteger(text_number.getText()) && text_number.getText().length() == 4) {
				// 確認數字不為重複,如果重複就設repeat = 1
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
					check++; // 次數加1
					// 確認每一個數字,如果數字與答案的位置相同則存入2,若位置不同但數字相同則存1,都沒有存0
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
					// 計算每一項的2和1的次數,分別為a和b
					int num_a = 0;
					int num_b = 0;
					for (int a = 0; a < 4; a++) {
						if (save_num[1][a] == 1)
							num_b++;
						else if (save_num[1][a] == 2)
							num_a++;
					}
					// 若4a則成功,將計時器停下後,將記錄加進紀錄欄中
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
						// 輸出幾a幾b,並將這次的紀錄加進此局遊戲的過程紀錄中
						answer_number.setText(num_a + "A" + num_b + "B");
						Label tem = new Label(text_number.getText() + " : " + answer_number.getText());
						tem.setStyle("-fx-font-size:18px;");
						tem.setMinHeight(30);
						record_number.getChildren().add(tem);
					}
				} else {
					// 如果數字重複則把輸入框改成紅色,提醒使用者輸入錯誤
					text_number.setStyle("-fx-border-color:#E53A40;");
				}
			} else {
				text_number.setStyle("-fx-border-color:#E53A40;");
			}

		} else {
			// 確認數字不為重複,如果重複就設repeat = 1
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
				check++; // 次數加1
				// 一個顏色對應一個數字,確認每一項的數字,如果數字與答案的位置相同則存入2,若位置不同但數字相同則存1,都沒有存0
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
				// 計算每一項的2和1的次數,分別為a和b
				int num_a = 0;
				int num_b = 0;
				for (int a = 0; a < 4; a++) {
					if (save_num[1][a] == 1)
						num_b++;
					else if (save_num[1][a] == 2)
						num_a++;
				}
				// 若4a則成功,將計時器停下後,將記錄加進紀錄欄中
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
					// 輸出幾a幾b,並將這次的紀錄加進此局遊戲的過程紀錄中
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
				// 如果數字重複則把輸入框改成紅色,提醒使用者輸入錯誤
				choice_color[0].setStyle("-fx-border-color:#E53A40; -fx-font-size:18px;");
				choice_color[1].setStyle("-fx-border-color:#E53A40; -fx-font-size:18px;");
				choice_color[2].setStyle("-fx-border-color:#E53A40; -fx-font-size:18px;");
				choice_color[3].setStyle("-fx-border-color:#E53A40; -fx-font-size:18px;");
			}
		}
	}

	// 重玩鍵,將所有按鈕設回預設型態
	public void restart() {
		check = 0;
		choice_color[0].getSelectionModel().select("白");
		choice_color[1].getSelectionModel().select("白");
		choice_color[2].getSelectionModel().select("白");
		choice_color[3].getSelectionModel().select("白");
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

	// 寫檔
	public void SaveFile(ArrayList<String> content, File file) {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(file);
			// 將讀檔的資料存入
			for (int a = 0; a < load_file.size(); a++) {
				fileWriter.write(load_file.get(a));
				fileWriter.write(System.getProperty("line.separator")); // 換行
			}
			// 將現有的新資料存入
			for (int a = 0; a < new_record.size(); a++) {
				fileWriter.write(new_record.get(a));
				fileWriter.write(System.getProperty("line.separator"));// 換行
			}
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 加入紀錄中
	public void addRecord() {
		String[] answer = new String[4];
		// 依玩的模式取得此局遊戲的答案
		if (status.equals("number")) {
			for (int a = 0; a < 4; a++) {
				answer[a] = String.valueOf(save_num[0][a]);
			}
		} else {
			for (int a = 0; a < 4; a++) {
				answer[a] = choice_color[0].getItems().get(save_num[0][a] - 1);
			}
		}
		// 將此紀錄存成label,並加入下方的紀錄欄中
		String tem_label = "姓名 : " + name.getText() + "   答案 : " + answer[0] + answer[1] + answer[2] + answer[3]
				+ "   次數 : " + check;
		new_record.add(tem_label);
		Label tem = new Label(tem_label);
		tem.setStyle("-fx-font-size:18px;");
		tem.setMinHeight(30);
		record_all.getChildren().add(tem);
	}
}