package cn.cstv.wspscm.monitor;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.views.navigator.ResourceNavigator;

import cn.cstv.wspscm.views.MessageView;
import cn.cstv.wspscm.views.NavigatorView;

/**
 * @author hp
 * 
 */
public class OutputRandomMessages {

	public void GenerateRandomMessages(String messageLogFileName,
			String ruleFileName, String errorRuleFileName,
			IWorkbenchWindow window, int number, int ratio) {
		List<MessageWithXY> allMessage = new ArrayList<MessageWithXY>();

		MessageWithXY startMessage = new MessageWithXY();

		MessageWithXY endMessage = new MessageWithXY();

		List<MessageSet> errorMessageSet = new ArrayList<MessageSet>();

		List<MessageSet> messageLogSet = GetMessageFromMessageLog(ruleFileName,
				errorRuleFileName, allMessage, startMessage, endMessage,
				errorMessageSet);

		OutputMessages(messageLogFileName, messageLogSet, errorMessageSet, window,
				number, ratio);

	}

	// public void GenerateRandomMessages(String timedMessagesFileName, String
	// outputFileName){
	// List<MessageWithXY> allMessage = new ArrayList<MessageWithXY>();
	//
	// MessageWithXY startMessage = new MessageWithXY();
	//
	// MessageWithXY endMessage = new MessageWithXY();
	//
	// List<MessageSet> messageLogSet = GetMessageFromMessageLog(
	// timedMessagesFileName, allMessage, startMessage, endMessage);
	//
	// OutputMessages(messageLogSet,outputFileName,number);
	// }
	/**
	 * @param args
	 */
	// public static void main(String[] args) {
	// // TODO Auto-generated method stub
	//
	// // for (int i = 0; i < 10; i++) {
	//
	// // long currentTime1 = System.currentTimeMillis();
	//
	// String timedMessagesFileName = "C:\\TimedMessages.rule";
	// String outputFileName = "C:\\OutputMessageFile.message";
	//
	// List<MessageWithXY> allMessage = new ArrayList<MessageWithXY>();
	//
	// MessageWithXY startMessage = new MessageWithXY();
	//
	// MessageWithXY endMessage = new MessageWithXY();
	//
	// List<MessageSet> messageLogSet = GetMessageFromMessageLog(
	// timedMessagesFileName, allMessage, startMessage, endMessage);
	//
	// int number = 50;
	// OutputMessages(messageLogSet, outputFileName, number);
	//
	// }
	/**
	 * @param messageLogSet
	 */
	private static void OutputMessages(String outputFileName,
			List<MessageSet> messageLogSet, List<MessageSet> errorMessageSet,
			IWorkbenchWindow window, int number, int ratio) {
		// TODO Auto-generated method stub
		// 注入错误的消息序列所在位置
		int errorMessageNumber = number * ratio / 100;
		if (errorMessageNumber == 0)
			errorMessageNumber = 1;
		int step = number / errorMessageNumber;

		int[] insertPosition = new int[errorMessageNumber];

		Random random = new Random();
		for (int i = 0; i < errorMessageNumber; i++) {
			insertPosition[i] = i * step + random.nextInt(step);
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date();
		long currentTime = date.getTime();
		List<MessageWithXY> currentMessage = new ArrayList<MessageWithXY>();
		List<Double> timeArray = new ArrayList<Double>();
		double maxTime = 0.0;
		MessageWithXY message = null;

		try {
			PrintWriter out = new PrintWriter(new FileWriter(outputFileName));

			double time = 0;
			double xInit = 0, yInit = 0;

			int size = messageLogSet.size();
			int errorSize = errorMessageSet.size();

			int kk = 0;
			for (int i = 0; i < number; i++) {
				MessageSet messageSet;
				if (i == insertPosition[kk]) {
					if (kk < errorMessageNumber - 1)
						kk++;
					int f = random.nextInt(errorSize);
					messageSet = errorMessageSet.get(f);
				} else {
					int k = random.nextInt(size);
					messageSet = messageLogSet.get(k);
				}

				for (int j = 0; j < messageSet.getMessage().size(); j++) {
					message = messageSet.getMessage().get(j);

					if (message.getTimedCondition().contains("x")) {
						if (message.getXSymbol() == 1) {
							xInit = 0;
							message.setTime((int) time);
						} else if (message.getXSymbol() == 0) {
							double xMaxNum = message.getX();
							double num = ((int) (Math.random()
							// * (xMaxNum - xInit) * 24 * 60 * 1000.0)) /
									// 1000.0;
									* (xMaxNum - xInit) * 60 * 1000.0)) / 1000.0;
							time += num;
							message.setTime((int) time);
							if (message.getYSymbol() == 3) {
								yInit = message.getY();
								message.setTime((int) time);
							}
						} else if (message.getXSymbol() == 3) {
							xInit = message.getX();
							message.setTime((int) time);
						} else {
							message.setTime((int) time);
						}
					}

					if (message.getTimedCondition().contains("y")) {
						if (message.getYSymbol() == 1) {
							yInit = 0;
							message.setTime((int) time);
						} else if (message.getYSymbol() == 0) {
							double yMaxNum = message.getY();
							double num = ((int) (Math.random()
							// * (yMaxNum - yInit) * 24 * 60 * 1000)) / 1000.0;
									* (yMaxNum - yInit) * 60 * 1000)) / 1000.0;
							time += num;

							message.setTime((int) time);
						} else {
							message.setTime((int) time);
						}
					}
					currentMessage.add(message);
					timeArray.add(new Double(message.getTime()));
					if (maxTime < message.getTime())
						maxTime = message.getTime();

				}
			}
			for (int i = 0; i < currentMessage.size(); i++) {
				message = currentMessage.get(i);
				out.println(sdf.format(currentTime
						- (maxTime - timeArray.get(i)) * 60 * 1000)
						+ " "
						+ (message.isMessageStatus() ? "[Monitor LOG]Entering:"
								: "[Monitor LOG]Exiting:")
						+ message.getMessageFullText());
			}
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		InputStream is;
		try {
			is = new FileInputStream(outputFileName);

			// TODO Auto-generated catch block

			byte[] inputByte = new byte[is.available()];

			is.read(inputByte);
			String contentString = new String(inputByte);
			IViewPart view = window.getActivePage().showView(MessageView.ID);
			((MessageView) view).getText().setText(contentString);
			is.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		IViewPart view1;
		try {
			view1 = window.getActivePage().showView(NavigatorView.ID);
			((ResourceNavigator) view1).getTreeViewer().setContentProvider(
					new WorkbenchContentProvider());
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// /**
	// * @param messageLogSet
	// */
	// private static void OutputMessages(List<MessageSet> messageLogSet,
	// String outputFileName, int number) {
	// // TODO Auto-generated method stub
	// SimpleDateFormat sdf =
	// new SimpleDateFormat("yyyy-MM-dd HH:mm");
	// Date date = new Date();
	// long currentTime = date.getTime();
	//
	// try {
	// PrintWriter out = new PrintWriter(new FileWriter(outputFileName));
	//
	// double time = 0;
	// double xInit = 0, yInit = 0;
	//
	// int size = messageLogSet.size();
	//
	// Random random = new Random();
	// for (int i = 0; i < number; i++) {
	// int k = random.nextInt(size);
	// MessageSet messageSet = messageLogSet.get(k);
	//
	// for (int j = 0; j < messageSet.getMessage().size(); j++) {
	// MessageWithXY message = messageSet.getMessage().get(j);
	//
	// if (message.getTimedCondition().contains("x")) {
	// if (message.getXSymbol() == 1) {
	// xInit = 0;
	// message.setTime((int) time);
	// } else if (message.getXSymbol() == 0) {
	// double xMaxNum = message.getX();
	// double num = ((int) (Math.random()
	// // * (xMaxNum - xInit) * 24 * 60 * 1000.0)) / 1000.0;
	// * (xMaxNum - xInit)* 60 * 1000.0)) / 1000.0;
	// time += num;
	// message.setTime((int) time);
	// if (message.getYSymbol() == 3) {
	// yInit = message.getY();
	// message.setTime((int) time);
	// }
	// } else if (message.getXSymbol() == 3) {
	// xInit = message.getX();
	// message.setTime((int) time);
	// } else {
	// message.setTime((int) time);
	// }
	// }
	//
	// if (message.getTimedCondition().contains("y")) {
	// if (message.getYSymbol() == 1) {
	// yInit = 0;
	// message.setTime((int) time);
	// } else if (message.getYSymbol() == 0) {
	// double yMaxNum = message.getY();
	// double num = ((int) (Math.random()
	// // * (yMaxNum - yInit) * 24 * 60 * 1000)) / 1000.0;
	// * (yMaxNum - yInit) * 60 * 1000)) / 1000.0;
	// time += num;
	//
	// message.setTime((int) time);
	// } else {
	// message.setTime((int) time);
	// }
	// }
	//
	//
	// out
	// .println(sdf.format(currentTime+message.getTime()*60*1000)
	// + " "
	// + (message.isMessageStatus() ? "[Monitor LOG]Entering:"
	// : "[Monitor LOG]Exiting:")
	// + message.getMessageFullText());
	// }
	//
	// }
	// out.close();
	// } catch (FileNotFoundException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	private static List<MessageSet> GetMessageFromMessageLog(
			String TimedMessagesFileName, String errorRuleFileName,
			List<MessageWithXY> allMessage, MessageWithXY startMessage,
			MessageWithXY endMessage, List<MessageSet> errorMessageSet) {
		// TODO Auto-generated method stub
		List<MessageSet> messageLogSet = new ArrayList<MessageSet>();

		try {
			BufferedReader messageReader = new BufferedReader(new FileReader(
					TimedMessagesFileName));

			String s = new String();
			String exiting = "[Monitor LOG]Exiting";
			String entering = "[Monitor LOG]Entering";

			int messageSetIndex = 0;

			MessageSet messageSet = new MessageSet(messageSetIndex);

			while ((s = messageReader.readLine()) != null) {
				if (s.length() == 0) {
					if (messageSet.getMessage().size() != 0) {
						messageLogSet.add(messageSet);
						messageSetIndex++;
						messageSet = new MessageSet(messageSetIndex);
					}

				} else {
					String messageTimedCondition = s.substring(0, s
							.indexOf("["));

					String messageStatusString = s.substring(s.indexOf("["), s
							.lastIndexOf(":"));

					boolean messageStatus = messageStatusString
							.equals(entering) ? true : false;

					String messageText = s.substring(s.lastIndexOf(":") + 1);

					String type = messageText.substring(messageText
							.indexOf("[") + 1, messageText.indexOf("]"));

					String messageFunction = messageText.substring(messageText
							.indexOf("]") + 1, messageText.indexOf("("));

					String parameter = messageText.substring(messageText
							.indexOf("(") + 1, messageText.indexOf(")"));

					if (messageTimedCondition.charAt(0) == ' ') { // 开始消息或结束消息，
						// 只有一次，
						// 没有在其他消息序列中出现
						if (messageSetIndex == 0) {
							startMessage = new MessageWithXY(type,
									messageFunction, parameter);
							if (messageStatusString.equals(entering)) {
								startMessage.setMessageStatus(true);
							} else if (messageStatusString.equals(exiting)) {
								startMessage.setMessageStatus(false);
							}
						} else {
							endMessage = new MessageWithXY(type,
									messageFunction, parameter);
							if (messageStatusString.equals(entering)) {
								endMessage.setMessageStatus(true);
							} else if (messageStatusString.equals(exiting)) {
								endMessage.setMessageStatus(false);
							}
						}
					} else {

						int messageContain = AllMessageContain(allMessage,
								messageText, messageStatus,
								messageTimedCondition);

						if (messageContain == -1) {
							MessageWithXY message = new MessageWithXY(type,
									messageFunction, parameter);
							if (messageStatusString.equals(entering)) {
								message.setMessageStatus(true);
							} else if (messageStatusString.equals(exiting)) {
								message.setMessageStatus(false);
							}
							message.setTimedCondition(messageTimedCondition);

							if (messageTimedCondition.contains("x")) {
								int xIndex = messageTimedCondition.indexOf("x");
								String symbol = messageTimedCondition
										.substring(xIndex + 1, xIndex + 2);
								if (symbol.equals("<")) {
									message.setXSymbol(0);
								} else if (symbol.equals("=")) {
									message.setXSymbol(1);
								} else if (symbol.equals(">")) {
									message.setXSymbol(2);
								} else if (symbol.equals(":")) {
									message.setXSymbol(3);
								}

								int xIntStart = xIndex + 2, xIntEnd;
								while (!Character.isDigit(messageTimedCondition
										.charAt(xIntStart))) {
									xIntStart++;
								}

								int xInt = xIntStart;
								while (Character.isDigit(messageTimedCondition
										.charAt(xInt))) {
									xInt++;
								}
								xIntEnd = xInt;

								message
										.setX(Integer
												.parseInt(messageTimedCondition
														.substring(xIntStart,
																xIntEnd)));

							}

							if (messageTimedCondition.contains("y")) {
								int yIndex = messageTimedCondition.indexOf("y");
								String symbol = messageTimedCondition
										.substring(yIndex + 1, yIndex + 2);
								if (symbol.equals("<")) {
									message.setYSymbol(0);
								} else if (symbol.equals("=")) {
									message.setYSymbol(1);
								} else if (symbol.equals(">")) {
									message.setYSymbol(2);
								} else if (symbol.equals(":")) {
									message.setYSymbol(3);
								}

								int yIntStart = yIndex + 2, yIntEnd;
								while (!Character.isDigit(messageTimedCondition
										.charAt(yIntStart))) {
									yIntStart++;
								}

								int yInt = yIntStart;
								while (Character.isDigit(messageTimedCondition
										.charAt(yInt))) {
									yInt++;
								}
								yIntEnd = yInt;
								message
										.setY(Integer
												.parseInt(messageTimedCondition
														.substring(yIntStart,
																yIntEnd)));
							}

							allMessage.add(message);

							messageSet.addMessage(message);
						} else {
							messageSet.addMessage(allMessage
									.get(messageContain));
						}
					}
				}
			}

			messageReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			BufferedReader errorRuleReader = new BufferedReader(new FileReader(
					errorRuleFileName));

			String s = new String();
			String exiting = "[Monitor LOG]Exiting";
			String entering = "[Monitor LOG]Entering";

			int messageSetIndex = 0;

			MessageSet messageSet = new MessageSet(messageSetIndex);

			while ((s = errorRuleReader.readLine()) != null) {
				if (s.length() == 0) {
					if (messageSet.getMessage().size() != 0) {
						errorMessageSet.add(messageSet);
						messageSetIndex++;
						messageSet = new MessageSet(messageSetIndex);
					}

				} else {
					String messageTimedCondition = s.substring(0, s
							.indexOf("["));

					String messageStatusString = s.substring(s.indexOf("["), s
							.lastIndexOf(":"));

					boolean messageStatus = messageStatusString
							.equals(entering) ? true : false;

					String messageText = s.substring(s.lastIndexOf(":") + 1);

					String type = messageText.substring(messageText
							.indexOf("[") + 1, messageText.indexOf("]"));

					String messageFunction = messageText.substring(messageText
							.indexOf("]") + 1, messageText.indexOf("("));

					String parameter = messageText.substring(messageText
							.indexOf("(") + 1, messageText.indexOf(")"));

					int messageContain = AllMessageContain(allMessage,
							messageText, messageStatus, messageTimedCondition);

					if (messageContain == -1) {
						MessageWithXY message = new MessageWithXY(type,
								messageFunction, parameter);
						if (messageStatusString.equals(entering)) {
							message.setMessageStatus(true);
						} else if (messageStatusString.equals(exiting)) {
							message.setMessageStatus(false);
						}
						message.setTimedCondition(messageTimedCondition);

						if (messageTimedCondition.contains("x")) {
							int xIndex = messageTimedCondition.indexOf("x");
							String symbol = messageTimedCondition.substring(
									xIndex + 1, xIndex + 2);
							if (symbol.equals("<")) {
								message.setXSymbol(0);
							} else if (symbol.equals("=")) {
								message.setXSymbol(1);
							} else if (symbol.equals(">")) {
								message.setXSymbol(2);
							} else if (symbol.equals(":")) {
								message.setXSymbol(3);
							}

							int xIntStart = xIndex + 2, xIntEnd;
							while (!Character.isDigit(messageTimedCondition
									.charAt(xIntStart))) {
								xIntStart++;
							}

							int xInt = xIntStart;
							while (Character.isDigit(messageTimedCondition
									.charAt(xInt))) {
								xInt++;
							}
							xIntEnd = xInt;

							message.setX(Integer.parseInt(messageTimedCondition
									.substring(xIntStart, xIntEnd)));

						}

						if (messageTimedCondition.contains("y")) {
							int yIndex = messageTimedCondition.indexOf("y");
							String symbol = messageTimedCondition.substring(
									yIndex + 1, yIndex + 2);
							if (symbol.equals("<")) {
								message.setYSymbol(0);
							} else if (symbol.equals("=")) {
								message.setYSymbol(1);
							} else if (symbol.equals(">")) {
								message.setYSymbol(2);
							} else if (symbol.equals(":")) {
								message.setYSymbol(3);
							}

							int yIntStart = yIndex + 2, yIntEnd;
							while (!Character.isDigit(messageTimedCondition
									.charAt(yIntStart))) {
								yIntStart++;
							}

							int yInt = yIntStart;
							while (Character.isDigit(messageTimedCondition
									.charAt(yInt))) {
								yInt++;
							}
							yIntEnd = yInt;
							message.setY(Integer.parseInt(messageTimedCondition
									.substring(yIntStart, yIntEnd)));
						}

						allMessage.add(message);

						messageSet.addMessage(message);
					} else {
						messageSet.addMessage(allMessage.get(messageContain));
					}
				}
			}
			if (messageSet.getMessage().size() != 0) {
				errorMessageSet.add(messageSet);
			}

			// errorMessage.SetMessageSet(messageSet);

			errorRuleReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		for (int i = 0; i < errorMessageSet.size(); i++) {
//			MessageSet temp = errorMessageSet.get(i);
//			for (int j = 0; j < temp.getMessage().size(); j++) {
//				System.out.println(temp.getMessage().get(j)
//						.getMessageFullText());
//			}
//			System.out.println();
//		}
		return messageLogSet;
	}

	private static int AllMessageContain(List<MessageWithXY> allMessage,
			String messageText, boolean messageStatus,
			String messageTimedCondition) {
		// TODO Auto-generated method stub
		if (allMessage.size() == 0)
			return -1;
		else {
			for (int i = 0; i < allMessage.size(); i++) {
				if (allMessage.get(i).getMessageFullText().equals(messageText)
						&& allMessage.get(i).getTimedCondition().equals(
								messageTimedCondition)
						&& allMessage.get(i).isMessageStatus() == messageStatus) {
					return i;
				}
			}
		}
		return -1;
	}

}
