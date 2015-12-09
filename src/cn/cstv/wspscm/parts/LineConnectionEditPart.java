/*************************************************************************
 * Copyright (c) 2006, 2008. All rights reserved. This program and the   
 * accompanying materials are made available under the terms of the      
 * Eclipse Public License v1.0 which accompanies this distribution,       
 * and is available at http://www.eclipse.org/legal/epl-v10.html         
 * 
 * Contributors:                                                         
 * Author: Su Zhiyong & Zhang Pengcheng                                 
 * Group: CSTV (Chair of Software Testing & Verification) Group          
 * E-mail: zhiyongsu@gmail.com, pchzhang@seu.edu.cn                     
 ***********************************************************************/

/***********************************************************************
 * Project: cn.cstv.wspscm                                          
 * Package: cn.cstv.wspscm.parts                                            
 * File: LineConnectionEditPart.java                                                   
 * Program: LineConnectionEditPart                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-7-22                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.parts;

import java.beans.PropertyChangeEvent;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;

import cn.cstv.wspscm.figure.OperatorFigure;
import cn.cstv.wspscm.figure.StrictFigure;
import cn.cstv.wspscm.figure.UnwantedMessageConstraintFigure;
import cn.cstv.wspscm.figure.LifelineConnectionAnchor;
import cn.cstv.wspscm.figure.MessageFigure;
import cn.cstv.wspscm.figure.MidpointOffsetLocator;
import cn.cstv.wspscm.figure.PresentConstraintFigure;
import cn.cstv.wspscm.figure.PresentFigure;
import cn.cstv.wspscm.figure.SourcePointOffsetLocator;
import cn.cstv.wspscm.figure.TargetPointOffsetLocator;
import cn.cstv.wspscm.figure.UnwantedChainConstraintFigure;
import cn.cstv.wspscm.figure.WantedChainConstraintFigure;
import cn.cstv.wspscm.model.Lifeline;
import cn.cstv.wspscm.model.LineConnection;
import cn.cstv.wspscm.policy.AttributeEditPolicy;
import cn.cstv.wspscm.policy.LineConnectionEditPolicy;
import cn.cstv.wspscm.policy.LineConnectionEndPointEditPolicy;

/**
 * @author Su Zhiyong & Zhang Pengcheng
 * 
 */
public class LineConnectionEditPart extends ConnectionEditPartWithListener {

	private LifelineConnectionAnchor sourceAnchor;
	private LifelineConnectionAnchor targetAnchor;

	/**
	 * @return the sourceAnchor
	 */
	public LifelineConnectionAnchor getSourceAnchor() {
		return sourceAnchor;
	}

	/**
	 * @param sourceAnchor
	 *            the sourceAnchor to set
	 */
	public void setSourceAnchor(LifelineConnectionAnchor sourceAnchor) {
		this.sourceAnchor = sourceAnchor;
	}

	/**
	 * @return the targetAnchor
	 */
	public LifelineConnectionAnchor getTargetAnchor() {
		return targetAnchor;
	}

	/**
	 * @param targetAnchor
	 *            the targetAnchor to set
	 */
	public void setTargetAnchor(LifelineConnectionAnchor targetAnchor) {
		this.targetAnchor = targetAnchor;
	}

	protected IFigure createFigure() {
		PolylineConnection connection = new PolylineConnection();
		connection.setTargetDecoration(new PolygonDecoration());
		return connection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
		installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE,
				new LineConnectionEndPointEditPolicy());
		installEditPolicy(EditPolicy.CONNECTION_ROLE,
				new LineConnectionEditPolicy());
		installEditPolicy(AttributeEditPolicy.SET_CONSTRAINT_ROLE,new AttributeEditPolicy());
	}

	public void propertyChange(PropertyChangeEvent event) {
		if (event.getPropertyName().equals(LineConnection.P_MESSAGE_TYPE)) {
			refreshVisuals();
		} else if (event.getPropertyName().equals(
				LineConnection.P_MESSAGE_VALUE)) {
			refreshVisuals();
//		} else if (event.getPropertyName().equals(
//				LineConnection.P_MESSAGE_RESET)) {
//			refreshVisuals();
		}else if (event.getPropertyName().equals(
				LineConnection.P_MESSAGE_PARA)) {
			refreshVisuals();
     	}

		else if (event.getPropertyName().equals(
				LineConnection.P_PRESENTCONSTRAINT_VALUE)) {
			refreshVisuals();
		} else if (event.getPropertyName().equals(
				LineConnection.P_PRESENTCONSTRAINT_CONSTRAINT)) {
			refreshVisuals();
		} else if (event.getPropertyName().equals(
				LineConnection.P_PRESENTCONSTRAINT_RESET)) {
			refreshVisuals();
		}
		
		/*
		 * 	else if (event.getPropertyName().equals(
				LineConnection.P_PASTBOOLEANCONSTRAINT_VALUE)) {
			refreshVisuals();
		} else if (event.getPropertyName().equals(
				LineConnection.P_PASTBOOLEANCONSTRAINT_CONSTRAINT)) {
			refreshVisuals();
		} else if (event.getPropertyName().equals(
				LineConnection.P_PASTBOOLEANCONSTRAINT_RESET)) {
			refreshVisuals();
		}
		else if (event.getPropertyName().equals(
				LineConnection.P_FUTUREBOOLEANCONSTRAINT_VALUE)) {
			refreshVisuals();
		} else if (event.getPropertyName().equals(
				LineConnection.P_FUTUREBOOLEANCONSTRAINT_CONSTRAINT)) {
			refreshVisuals();
		} else if (event.getPropertyName().equals(
				LineConnection.P_FUTUREBOOLEANCONSTRAINT_RESET)) {
			refreshVisuals();
		}
		 * */
		
		else if (event.getPropertyName().equals(
				LineConnection.P_PASTUNWANTEDMESSAGECONSTRAINT_VALUE)) {
			refreshVisuals();
		} else if (event.getPropertyName().equals(
				LineConnection.P_PASTUNWANTEDMESSAGECONSTRAINT_CONSTRAINT)) {
			refreshVisuals();
		} else if (event.getPropertyName().equals(
				LineConnection.P_PASTUNWANTEDMESSAGECONSTRAINT_RESET)) {
			refreshVisuals();
		}

		else if (event.getPropertyName().equals(
				LineConnection.P_FUTUREUNWANTEDMESSAGECONSTRAINT_VALUE)) {
			refreshVisuals();
		} else if (event.getPropertyName().equals(
				LineConnection.P_FUTUREUNWANTEDMESSAGECONSTRAINT_CONSTRAINT)) {
			refreshVisuals();
		} else if (event.getPropertyName().equals(
				LineConnection.P_FUTUREUNWANTEDMESSAGECONSTRAINT_RESET)) {
			refreshVisuals();
		}

		else if (event.getPropertyName().equals(
				LineConnection.P_PRESENTPAST_VALUE)) {
			refreshVisuals();
		} else if (event.getPropertyName().equals(
				LineConnection.P_PRESENTPAST_CONSTRAINT)) {
			refreshVisuals();
		} else if (event.getPropertyName().equals(
				LineConnection.P_PRESENTPAST_RESET)) {
			refreshVisuals();
		}

		else if (event.getPropertyName().equals(
				LineConnection.P_PRESENTFUTURE_VALUE)) {
			refreshVisuals();
		} else if (event.getPropertyName().equals(
				LineConnection.P_PRESENTFUTURE_CONSTRAINT)) {
			refreshVisuals();
		} else if (event.getPropertyName().equals(
				LineConnection.P_PRESENTFUTURE_RESET)) {
			refreshVisuals();
		}

		else if (event.getPropertyName().equals(
				LineConnection.P_PASTWANTEDCHAINCONSTRAINT_VALUE)) {
			refreshVisuals();
		} else if (event.getPropertyName().equals(
				LineConnection.P_PASTWANTEDCHAINCONSTRAINT_CONSTRAINT)) {
			refreshVisuals();
		} else if (event.getPropertyName().equals(
				LineConnection.P_PASTWANTEDCHAINCONSTRAINT_RESET)) {
			refreshVisuals();
		}

		else if (event.getPropertyName().equals(
				LineConnection.P_FUTUREWANTEDCHAINCONSTRAINT_VALUE)) {
			refreshVisuals();
		} else if (event.getPropertyName().equals(
				LineConnection.P_FUTUREWANTEDCHAINCONSTRAINT_CONSTRAINT)) {
			refreshVisuals();
		} else if (event.getPropertyName().equals(
				LineConnection.P_FUTUREWANTEDCHAINCONSTRAINT_RESET)) {
			refreshVisuals();
		}

		else if (event.getPropertyName().equals(
				LineConnection.P_PASTUNWANTEDCHAINCONSTRAINT_VALUE)) {
			refreshVisuals();
		} else if (event.getPropertyName().equals(
				LineConnection.P_PASTUNWANTEDCHAINCONSTRAINT_CONSTRAINT)) {
			refreshVisuals();
		} else if (event.getPropertyName().equals(
				LineConnection.P_PASTUNWANTEDCHAINCONSTRAINT_RESET)) {
			refreshVisuals();
		}

		else if (event.getPropertyName().equals(
				LineConnection.P_FUTUREUNWANTEDCHAINCONSTRAINT_VALUE)) {
			refreshVisuals();
		} else if (event.getPropertyName().equals(
				LineConnection.P_FUTUREUNWANTEDCHAINCONSTRAINT_CONSTRAINT)) {
			refreshVisuals();
		} else if (event.getPropertyName().equals(
				LineConnection.P_FUTUREUNWANTEDCHAINCONSTRAINT_RESET)) {
			refreshVisuals();
		}
		else if (event.getPropertyName().equals(
				LineConnection.P_ISSTRICT)) {
			refreshVisuals();
		}

		else {
			refreshVisuals();
		}
	}
	
	public String getMessageType(){
		if(((LineConnection) getModel())
				.getMessageType()==0){
			return "e:";
		}else if (((LineConnection) getModel())
						.getMessageType()==1){
			return "r:";
		}else if (((LineConnection) getModel())
						.getMessageType()==2){
			return "f:";
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void refreshVisuals() {
		// ����������Ա�־
		List figures = ((PolylineConnection) getFigure()).getChildren();
		for (int i = 0; i < figures.size(); i++) {
			if (figures.get(i) instanceof PresentConstraintFigure) {
				((PolylineConnection) getFigure()).remove((IFigure) figures
						.get(i));
			} else if (figures.get(i) instanceof WantedChainConstraintFigure) {
				((PolylineConnection) getFigure()).remove((IFigure) figures
						.get(i));
			} else if (figures.get(i) instanceof UnwantedChainConstraintFigure) {
				((PolylineConnection) getFigure()).remove((IFigure) figures
						.get(i));
			} else if (figures.get(i) instanceof UnwantedMessageConstraintFigure) {
				((PolylineConnection) getFigure()).remove((IFigure) figures
						.get(i));
			} else if (figures.get(i) instanceof PresentFigure) {
				((PolylineConnection) getFigure()).remove((IFigure) figures
						.get(i));
			} else if (figures.get(i) instanceof MessageFigure) {
				((PolylineConnection) getFigure()).remove((IFigure) figures
						.get(i));
			}else if (figures.get(i) instanceof OperatorFigure) {
				((PolylineConnection) getFigure()).remove((IFigure) figures
						.get(i));
			}else if (figures.get(i) instanceof StrictFigure) {
				((PolylineConnection) getFigure()).remove((IFigure) figures
						.get(i));
			}
		}

		// �ٴ�����������Ա�־��ǰ�����һ���ɾ�
		figures = ((PolylineConnection) getFigure()).getChildren();
		for (int i = 0; i < figures.size(); i++) {
			if (figures.get(i) instanceof PresentConstraintFigure) {
				((PolylineConnection) getFigure()).remove((IFigure) figures
						.get(i));
			} else if (figures.get(i) instanceof WantedChainConstraintFigure) {
				((PolylineConnection) getFigure()).remove((IFigure) figures
						.get(i));
			} else if (figures.get(i) instanceof UnwantedChainConstraintFigure) {
				((PolylineConnection) getFigure()).remove((IFigure) figures
						.get(i));
			} else if (figures.get(i) instanceof UnwantedMessageConstraintFigure) {
				((PolylineConnection) getFigure()).remove((IFigure) figures
						.get(i));
			} else if (figures.get(i) instanceof PresentFigure) {
				((PolylineConnection) getFigure()).remove((IFigure) figures
						.get(i));
			} else if (figures.get(i) instanceof MessageFigure) {
				((PolylineConnection) getFigure()).remove((IFigure) figures
						.get(i));
			}else if (figures.get(i) instanceof OperatorFigure) {
				((PolylineConnection) getFigure()).remove((IFigure) figures
						.get(i));
			}else if (figures.get(i) instanceof StrictFigure) {
				((PolylineConnection) getFigure()).remove((IFigure) figures
						.get(i));
			} 
		}

		if (!((LineConnection) getModel()).getMessageValue().isEmpty()) {
			Label text = null;
			if (((LineConnection) getModel()).getMessagePara().isEmpty()
					) {
				text = new MessageFigure(getMessageType() + ((LineConnection) getModel())
						.getMessageValue() + "(" + ")");

			} else if ((!((LineConnection) getModel()).getMessagePara()
					.isEmpty())
					) {
				text = new MessageFigure(getMessageType() + ((LineConnection) getModel())
						.getMessageValue()
						+ "("
						+ ((LineConnection) getModel()).getMessagePara() + ")");

			} 
			/*
			else if (((LineConnection) getModel()).getMessagePara()
					.isEmpty()
					&& (!(((LineConnection) getModel()).getMessageType()
							==0))) {
				text = new MessageFigure(getMessageType() + ((LineConnection) getModel())
						.getMessageValue()
						+ ";" + ((LineConnection) getModel()).getMessageType());

			} else if ((!((LineConnection) getModel()).getMessagePara()
					.isEmpty())
					&& (!(((LineConnection) getModel()).getMessageType()
							==0))) {
				text = new MessageFigure(getMessageType() + ((LineConnection) getModel())
						.getMessageValue()
						+ ";"
						+ ((LineConnection) getModel()).getMessagePara()
						+ ";" + ((LineConnection) getModel()).getMessageType());
			}
			*/
			
			((PolylineConnection) getFigure()).add(text,
					new MidpointOffsetLocator(
							((PolylineConnection) getFigure()), 0, new Point(0,
									-15)));
		}

		if (!((LineConnection) getModel()).getPresentConstraintValue()
				.isEmpty()) {
			Label middleLabel = null;
			if (((LineConnection) getModel()).getPresentConstraintConstraint()
					.isEmpty()
					&& ((LineConnection) getModel())
							.getPresentConstraintReset().isEmpty()) {
				middleLabel = new PresentConstraintFigure(
						ForShort(((LineConnection) getModel())
								.getPresentConstraintValue()));

			} else if ((!((LineConnection) getModel())
					.getPresentConstraintConstraint().isEmpty())
					&& ((LineConnection) getModel())
							.getPresentConstraintReset().isEmpty()) {
				middleLabel = new PresentConstraintFigure(
						ForShort(((LineConnection) getModel())
								.getPresentConstraintValue())
								+ ";"
								+ ForShort(((LineConnection) getModel())
										.getPresentConstraintConstraint()));

			} else if (((LineConnection) getModel())
					.getPresentConstraintConstraint().isEmpty()
					&& (!((LineConnection) getModel())
							.getPresentConstraintReset().isEmpty())) {
				middleLabel = new PresentConstraintFigure(
						ForShort(((LineConnection) getModel())
								.getPresentConstraintValue())
								+ ";"
								+ ForShort(((LineConnection) getModel())
										.getPresentConstraintReset()));

			} else if ((!((LineConnection) getModel())
					.getPresentConstraintConstraint().isEmpty())
					&& (!((LineConnection) getModel())
							.getPresentConstraintReset().isEmpty())) {
				middleLabel = new PresentConstraintFigure(
						ForShort(((LineConnection) getModel())
								.getPresentConstraintValue())
								+ ";"
								+ ForShort(((LineConnection) getModel())
										.getPresentConstraintConstraint())
								+ ";"
								+ ForShort(((LineConnection) getModel())
										.getPresentConstraintReset()));
			}
			((PolylineConnection) getFigure()).add(middleLabel,
					new MidpointOffsetLocator(
							((PolylineConnection) getFigure()), 0, new Point(0,
									6)));
		}

		if (!((LineConnection) getModel()).getPastUnwantedMessageConstraintValue()
				.isEmpty()) {
			IFigure endPointLabel = null;
			if (((LineConnection) getModel())
					.getPastUnwantedMessageConstraintConstraint().isEmpty()
					&& ((LineConnection) getModel())
							.getPastUnwantedMessageConstraintReset().isEmpty()) {
				endPointLabel = new UnwantedMessageConstraintFigure(
						ForShort(((LineConnection) getModel())
								.getPastUnwantedMessageConstraintValue()));

			} else if ((!((LineConnection) getModel())
					.getPastUnwantedMessageConstraintConstraint().isEmpty())
					&& ((LineConnection) getModel())
							.getPastUnwantedMessageConstraintReset().isEmpty()) {
				endPointLabel = new UnwantedMessageConstraintFigure(
						ForShort(((LineConnection) getModel())
								.getPastUnwantedMessageConstraintValue())
								+ ";"
								+ ForShort(((LineConnection) getModel())
										.getPastUnwantedMessageConstraintConstraint()));

			} else if (((LineConnection) getModel())
					.getPastUnwantedMessageConstraintConstraint().isEmpty()
					&& (!((LineConnection) getModel())
							.getPastUnwantedMessageConstraintReset().isEmpty())) {
				endPointLabel = new UnwantedMessageConstraintFigure(
						ForShort(((LineConnection) getModel())
								.getPastUnwantedMessageConstraintValue())
								+ ";"
								+ ForShort(((LineConnection) getModel())
										.getPastUnwantedMessageConstraintReset()));

			} else if ((!((LineConnection) getModel())
					.getPastUnwantedMessageConstraintConstraint().isEmpty())
					&& (!((LineConnection) getModel())
							.getPastUnwantedMessageConstraintReset().isEmpty())) {
				endPointLabel = new UnwantedMessageConstraintFigure(
						ForShort(((LineConnection) getModel())
								.getPastUnwantedMessageConstraintValue())
								+ ";"
								+ ForShort(((LineConnection) getModel())
										.getPastUnwantedMessageConstraintConstraint())
								+ ";"
								+ ForShort(((LineConnection) getModel())
										.getPastUnwantedMessageConstraintReset()));
			}

			// ConnectionEndpointLocator endpointLocator = new
			// ConnectionEndpointLocator(
			// ((PolylineConnection) getFigure()), false);
			// if (((PolylineConnection) getFigure()).getSourceAnchor()
			// .getReferencePoint().x < ((PolylineConnection) getFigure())
			// .getTargetAnchor().getReferencePoint().x) {
			// endpointLocator.setUDistance(-20);
			// endpointLocator.setVDistance(14);
			// } else {
			// endpointLocator.setUDistance(-20);
			// endpointLocator.setVDistance(-26);
			// }
			// ((PolylineConnection) getFigure()).add(endPointLabel,
			// endpointLocator);
			endPointLabel.setSize(80, 30);
			((PolylineConnection) getFigure()).add(endPointLabel,
					new SourcePointOffsetLocator(
							((PolylineConnection) getFigure()), 0, new Point(15,
									8)));
		}

		if (!((LineConnection) getModel()).getFutureUnwantedMessageConstraintValue()
				.isEmpty()) {
			IFigure endPointLabel = null;
			if (((LineConnection) getModel())
					.getFutureUnwantedMessageConstraintConstraint().isEmpty()
					&& ((LineConnection) getModel())
							.getFutureUnwantedMessageConstraintReset().isEmpty()) {
				endPointLabel = new UnwantedMessageConstraintFigure(
						ForShort(((LineConnection) getModel())
								.getFutureUnwantedMessageConstraintValue()));

			} else if ((!((LineConnection) getModel())
					.getFutureUnwantedMessageConstraintConstraint().isEmpty())
					&& ((LineConnection) getModel())
							.getFutureUnwantedMessageConstraintReset().isEmpty()) {
				endPointLabel = new UnwantedMessageConstraintFigure(
						ForShort(((LineConnection) getModel())
								.getFutureUnwantedMessageConstraintValue())
								+ ";"
								+ ForShort(((LineConnection) getModel())
										.getFutureUnwantedMessageConstraintConstraint()));

			} else if (((LineConnection) getModel())
					.getFutureUnwantedMessageConstraintConstraint().isEmpty()
					&& (!((LineConnection) getModel())
							.getFutureUnwantedMessageConstraintReset().isEmpty())) {
				endPointLabel = new UnwantedMessageConstraintFigure(
						ForShort(((LineConnection) getModel())
								.getFutureUnwantedMessageConstraintValue())
								+ ";"
								+ ForShort(((LineConnection) getModel())
										.getFutureUnwantedMessageConstraintReset()));

			} else if ((!((LineConnection) getModel())
					.getFutureUnwantedMessageConstraintConstraint().isEmpty())
					&& (!((LineConnection) getModel())
							.getFutureUnwantedMessageConstraintReset().isEmpty())) {
				endPointLabel = new UnwantedMessageConstraintFigure(
						ForShort(((LineConnection) getModel())
								.getFutureUnwantedMessageConstraintValue())
								+ ";"
								+ ForShort(((LineConnection) getModel())
										.getFutureUnwantedMessageConstraintConstraint())
								+ ";"
								+ ForShort(((LineConnection) getModel())
										.getFutureUnwantedMessageConstraintReset()));
			}
			// ConnectionEndpointLocator endpointLocator = new
			// ConnectionEndpointLocator(
			// ((PolylineConnection) getFigure()), true);
			// if (((PolylineConnection) getFigure()).getSourceAnchor()
			// .getReferencePoint().x < ((PolylineConnection) getFigure())
			// .getTargetAnchor().getReferencePoint().x) {
			// endpointLocator.setUDistance(-20);
			// endpointLocator.setVDistance(-26);
			// } else {
			// endpointLocator.setUDistance(-20);
			// endpointLocator.setVDistance(14);
			// }
			// ((PolylineConnection) getFigure()).add(endPointLabel,
			// endpointLocator);
			endPointLabel.setSize(80, 30);
			((PolylineConnection) getFigure()).add(endPointLabel,
					new TargetPointOffsetLocator(
							((PolylineConnection) getFigure()), 0, new Point(40,
									8)));

		}
		
		if (!((LineConnection) getModel()).isStrict()
				== false) {
			
			
			IFigure endPointLabel = new StrictFigure("S");
			endPointLabel.setSize(80, 30);
			((PolylineConnection) getFigure()).add(endPointLabel,
					new TargetPointOffsetLocator(
							((PolylineConnection) getFigure()), 0, new Point(40,
									8)));

		}

		if (!((LineConnection) getModel()).getPresentPastValue().isEmpty()) {
			IFigure endPointLabel = null;
			if (((LineConnection) getModel()).getPresentPastConstraint()
					.isEmpty()
					&& ((LineConnection) getModel()).getPresentPastReset()
							.isEmpty()) {
				endPointLabel = new PresentFigure(
						ForShort(((LineConnection) getModel())
								.getPresentPastValue()));

			} else if ((!((LineConnection) getModel())
					.getPresentPastConstraint().isEmpty())
					&& ((LineConnection) getModel()).getPresentPastReset()
							.isEmpty()) {
				endPointLabel = new PresentFigure(
						ForShort(((LineConnection) getModel())
								.getPresentPastValue())
								+ ";"
								+ ForShort(((LineConnection) getModel())
										.getPresentPastConstraint()));

			} else if (((LineConnection) getModel()).getPresentPastConstraint()
					.isEmpty()
					&& (!((LineConnection) getModel()).getPresentPastReset()
							.isEmpty())) {
				endPointLabel = new PresentFigure(
						ForShort(((LineConnection) getModel())
								.getPresentPastValue())
								+ ";"
								+ ForShort(((LineConnection) getModel())
										.getPresentPastReset()));

			} else if ((!((LineConnection) getModel())
					.getPresentPastConstraint().isEmpty())
					&& (!((LineConnection) getModel()).getPresentPastReset()
							.isEmpty())) {
				endPointLabel = new PresentFigure(
						ForShort(((LineConnection) getModel())
								.getPresentPastValue())
								+ ";"
								+ ForShort(((LineConnection) getModel())
										.getPresentPastConstraint())
								+ ";"
								+ ForShort(((LineConnection) getModel())
										.getPresentPastReset()));
			}
			// ConnectionEndpointLocator endpointLocator = new
			// ConnectionEndpointLocator(
			// ((PolylineConnection) getFigure()), false);
			// if (((PolylineConnection) getFigure()).getSourceAnchor()
			// .getReferencePoint().x < ((PolylineConnection) getFigure())
			// .getTargetAnchor().getReferencePoint().x) {
			// endpointLocator.setUDistance(-20);
			// endpointLocator.setVDistance(14);
			// } else {
			// endpointLocator.setUDistance(-20);
			// endpointLocator.setVDistance(-26);
			// }
			// ((PolylineConnection) getFigure()).add(endPointLabel,
			// endpointLocator);
			((PolylineConnection) getFigure()).add(endPointLabel,
					new SourcePointOffsetLocator(
							((PolylineConnection) getFigure()), 0, new Point(0,
									6)));
		}

		if (!((LineConnection) getModel()).getPresentFutureValue().isEmpty()) {
			IFigure endPointLabel = null;
			if (((LineConnection) getModel()).getPresentFutureConstraint()
					.isEmpty()
					&& ((LineConnection) getModel()).getPresentFutureReset()
							.isEmpty()) {
				endPointLabel = new PresentFigure(
						ForShort(((LineConnection) getModel())
								.getPresentFutureValue()));

			} else if ((!((LineConnection) getModel())
					.getPresentFutureConstraint().isEmpty())
					&& ((LineConnection) getModel()).getPresentFutureReset()
							.isEmpty()) {
				endPointLabel = new PresentFigure(
						ForShort(((LineConnection) getModel())
								.getPresentFutureValue())
								+ ";"
								+ ForShort(((LineConnection) getModel())
										.getPresentFutureConstraint()));

			} else if (((LineConnection) getModel())
					.getPresentFutureConstraint().isEmpty()
					&& (!((LineConnection) getModel()).getPresentFutureReset()
							.isEmpty())) {
				endPointLabel = new PresentFigure(
						ForShort(((LineConnection) getModel())
								.getPresentFutureValue())
								+ ";"
								+ ForShort(((LineConnection) getModel())
										.getPresentFutureReset()));

			} else if ((!((LineConnection) getModel())
					.getPresentFutureConstraint().isEmpty())
					&& (!((LineConnection) getModel()).getPresentFutureReset()
							.isEmpty())) {
				endPointLabel = new PresentFigure(
						ForShort(((LineConnection) getModel())
								.getPresentFutureValue())
								+ ";"
								+ ForShort(((LineConnection) getModel())
										.getPresentFutureConstraint())
								+ ";"
								+ ForShort(((LineConnection) getModel())
										.getPresentFutureReset()));
			}
			// ConnectionEndpointLocator endpointLocator = new
			// ConnectionEndpointLocator(
			// ((PolylineConnection) getFigure()), true);
			// if (((PolylineConnection) getFigure()).getSourceAnchor()
			// .getReferencePoint().x < ((PolylineConnection) getFigure())
			// .getTargetAnchor().getReferencePoint().x) {
			// endpointLocator.setUDistance(-20);
			// endpointLocator.setVDistance(-26);
			// } else {
			// endpointLocator.setUDistance(-20);
			// endpointLocator.setVDistance(14);
			// }
			// ((PolylineConnection) getFigure()).add(endPointLabel,
			// endpointLocator);
			((PolylineConnection) getFigure()).add(endPointLabel,
					new TargetPointOffsetLocator(
							((PolylineConnection) getFigure()), 0, new Point(0,
									6)));

		}

		if (!((LineConnection) getModel()).getPastWantedChainConstraintValue()
				.isEmpty()) {
			boolean temp = false;
			if (((PolylineConnection) getFigure()).getSourceAnchor()
					.getReferencePoint().x < ((PolylineConnection) getFigure())
					.getTargetAnchor().getReferencePoint().x) {
				temp = true;
			}

			IFigure endPointLabel = null;
			if (((LineConnection) getModel())
					.getPastWantedChainConstraintConstraint().isEmpty()
					&& ((LineConnection) getModel())
							.getPastWantedChainConstraintReset().isEmpty()) {
				endPointLabel = new WantedChainConstraintFigure(temp,
						ForShort(((LineConnection) getModel())
								.getPastWantedChainConstraintValue()));

			} else if ((!((LineConnection) getModel())
					.getPastWantedChainConstraintConstraint().isEmpty())
					&& ((LineConnection) getModel())
							.getPastWantedChainConstraintReset().isEmpty()) {
				endPointLabel = new WantedChainConstraintFigure(
						temp,
						ForShort(((LineConnection) getModel())
								.getPastWantedChainConstraintValue())
								+ ";"
								+ ForShort(((LineConnection) getModel())
										.getPastWantedChainConstraintConstraint()));

			} else if (((LineConnection) getModel())
					.getPastWantedChainConstraintConstraint().isEmpty()
					&& (!((LineConnection) getModel())
							.getPastWantedChainConstraintReset().isEmpty())) {
				endPointLabel = new WantedChainConstraintFigure(temp,
						ForShort(((LineConnection) getModel())
								.getPastWantedChainConstraintValue())
								+ ";"
								+ ForShort(((LineConnection) getModel())
										.getPastWantedChainConstraintReset()));

			} else if ((!((LineConnection) getModel())
					.getPastWantedChainConstraintConstraint().isEmpty())
					&& (!((LineConnection) getModel())
							.getPastWantedChainConstraintReset().isEmpty())) {
				endPointLabel = new WantedChainConstraintFigure(
						temp,
						ForShort(((LineConnection) getModel())
								.getPastWantedChainConstraintValue())
								+ ";"
								+ ForShort(((LineConnection) getModel())
										.getPastWantedChainConstraintConstraint())
								+ ";"
								+ ForShort(((LineConnection) getModel())
										.getPastWantedChainConstraintReset()));
			}

			// ConnectionEndpointLocator endpointLocator = new
			// ConnectionEndpointLocator(
			// ((PolylineConnection) getFigure()), false);
			// if (temp) {
			// endpointLocator.setUDistance(-36);
			// } else {
			// endpointLocator.setUDistance(-36);
			// endpointLocator.setVDistance(-60);
			// }
			// ((PolylineConnection) getFigure()).add(endPointLabel,
			// endpointLocator);
			endPointLabel.setSize(80, 50);
			((PolylineConnection) getFigure()).add(endPointLabel,
					new SourcePointOffsetLocator(
							((PolylineConnection) getFigure()), 0, new Point(0,
									23)));

		}

		if (!((LineConnection) getModel())
				.getFutureWantedChainConstraintValue().isEmpty()) {
			boolean temp = false;
			if (((PolylineConnection) getFigure()).getSourceAnchor()
					.getReferencePoint().x < ((PolylineConnection) getFigure())
					.getTargetAnchor().getReferencePoint().x) {
				temp = true;
			}

			IFigure endPointLabel = null;
			if (((LineConnection) getModel())
					.getFutureWantedChainConstraintConstraint().isEmpty()
					&& ((LineConnection) getModel())
							.getFutureWantedChainConstraintReset().isEmpty()) {
				endPointLabel = new WantedChainConstraintFigure(temp,
						ForShort(((LineConnection) getModel())
								.getFutureWantedChainConstraintValue()));

			} else if ((!((LineConnection) getModel())
					.getFutureWantedChainConstraintConstraint().isEmpty())
					&& ((LineConnection) getModel())
							.getFutureWantedChainConstraintReset().isEmpty()) {
				endPointLabel = new WantedChainConstraintFigure(
						temp,
						ForShort(((LineConnection) getModel())
								.getFutureWantedChainConstraintValue())
								+ ";"
								+ ForShort(((LineConnection) getModel())
										.getFutureWantedChainConstraintConstraint()));

			} else if (((LineConnection) getModel())
					.getFutureWantedChainConstraintConstraint().isEmpty()
					&& (!((LineConnection) getModel())
							.getFutureWantedChainConstraintReset().isEmpty())) {
				endPointLabel = new WantedChainConstraintFigure(temp,
						ForShort(((LineConnection) getModel())
								.getFutureWantedChainConstraintValue())
								+ ";"
								+ ForShort(((LineConnection) getModel())
										.getFutureWantedChainConstraintReset()));

			} else if ((!((LineConnection) getModel())
					.getFutureWantedChainConstraintConstraint().isEmpty())
					&& (!((LineConnection) getModel())
							.getFutureWantedChainConstraintReset().isEmpty())) {
				endPointLabel = new WantedChainConstraintFigure(
						temp,
						ForShort(((LineConnection) getModel())
								.getFutureWantedChainConstraintValue())
								+ ";"
								+ ForShort(((LineConnection) getModel())
										.getFutureWantedChainConstraintConstraint())
								+ ";"
								+ ForShort(((LineConnection) getModel())
										.getFutureWantedChainConstraintReset()));
			}
			// ConnectionEndpointLocator endpointLocator1 = new
			// ConnectionEndpointLocator(
			// ((PolylineConnection) getFigure()), true);
			// if (temp) {
			// endpointLocator1.setUDistance(-36);
			// endpointLocator1.setVDistance(-60);
			// } else {
			// endpointLocator1.setUDistance(-36);
			// }
			//
			// ((PolylineConnection) getFigure()).add(endPointLabel,
			// endpointLocator1);
			endPointLabel.setSize(80, 50);
			((PolylineConnection) getFigure()).add(endPointLabel,
					new TargetPointOffsetLocator(
							((PolylineConnection) getFigure()), 0, new Point(0,
									23)));

		}

		if (!((LineConnection) getModel())
				.getPastUnwantedChainConstraintValue().isEmpty()) {
			boolean temp = false;
			if (((PolylineConnection) getFigure()).getSourceAnchor()
					.getReferencePoint().x < ((PolylineConnection) getFigure())
					.getTargetAnchor().getReferencePoint().x) {
				temp = true;
			}

			IFigure endPointLabel = null;
			if (((LineConnection) getModel())
					.getPastUnwantedChainConstraintConstraint().isEmpty()
					&& ((LineConnection) getModel())
							.getPastUnwantedChainConstraintReset().isEmpty()) {
				endPointLabel = new UnwantedChainConstraintFigure(temp,
						ForShort(((LineConnection) getModel())
								.getPastUnwantedChainConstraintValue()));

			} else if ((!((LineConnection) getModel())
					.getPastUnwantedChainConstraintConstraint().isEmpty())
					&& ((LineConnection) getModel())
							.getPastUnwantedChainConstraintReset().isEmpty()) {
				endPointLabel = new UnwantedChainConstraintFigure(
						temp,
						ForShort(((LineConnection) getModel())
								.getPastUnwantedChainConstraintValue())
								+ ";"
								+ ForShort(((LineConnection) getModel())
										.getPastUnwantedChainConstraintConstraint()));

			} else if (((LineConnection) getModel())
					.getPastUnwantedChainConstraintConstraint().isEmpty()
					&& (!((LineConnection) getModel())
							.getPastUnwantedChainConstraintReset().isEmpty())) {
				endPointLabel = new UnwantedChainConstraintFigure(temp,
						ForShort(((LineConnection) getModel())
								.getPastUnwantedChainConstraintValue())
								+ ";"
								+ ForShort(((LineConnection) getModel())
										.getPastUnwantedChainConstraintReset()));

			} else if ((!((LineConnection) getModel())
					.getPastUnwantedChainConstraintConstraint().isEmpty())
					&& (!((LineConnection) getModel())
							.getPastUnwantedChainConstraintReset().isEmpty())) {
				endPointLabel = new UnwantedChainConstraintFigure(
						temp,
						ForShort(((LineConnection) getModel())
								.getPastUnwantedChainConstraintValue())
								+ ";"
								+ ForShort(((LineConnection) getModel())
										.getPastUnwantedChainConstraintConstraint())
								+ ";"
								+ ForShort(((LineConnection) getModel())
										.getPastUnwantedChainConstraintReset()));
			}

			// ConnectionEndpointLocator endpointLocator = new
			// ConnectionEndpointLocator(
			// ((PolylineConnection) getFigure()), false);
			// if (temp) {
			// endpointLocator.setUDistance(-36);
			// } else {
			// endpointLocator.setUDistance(-36);
			// endpointLocator.setVDistance(-60);
			// }
			// ((PolylineConnection) getFigure()).add(endPointLabel,
			// endpointLocator);
			endPointLabel.setSize(80, 50);
			((PolylineConnection) getFigure()).add(endPointLabel,
					new SourcePointOffsetLocator(
							((PolylineConnection) getFigure()), 0, new Point(0,
									23)));

		}

		if (!((LineConnection) getModel())
				.getFutureUnwantedChainConstraintValue().isEmpty()) {
			boolean temp = false;
			if (((PolylineConnection) getFigure()).getSourceAnchor()
					.getReferencePoint().x < ((PolylineConnection) getFigure())
					.getTargetAnchor().getReferencePoint().x) {
				temp = true;
			}

			IFigure endPointLabel = null;
			if (((LineConnection) getModel())
					.getFutureUnwantedChainConstraintConstraint().isEmpty()
					&& ((LineConnection) getModel())
							.getFutureUnwantedChainConstraintReset().isEmpty()) {
				endPointLabel = new UnwantedChainConstraintFigure(temp,
						ForShort(((LineConnection) getModel())
								.getFutureUnwantedChainConstraintValue()));

			} else if ((!((LineConnection) getModel())
					.getFutureUnwantedChainConstraintConstraint().isEmpty())
					&& ((LineConnection) getModel())
							.getFutureUnwantedChainConstraintReset().isEmpty()) {
				endPointLabel = new UnwantedChainConstraintFigure(
						temp,
						ForShort(((LineConnection) getModel())
								.getFutureUnwantedChainConstraintValue())
								+ ";"
								+ ForShort(((LineConnection) getModel())
										.getFutureUnwantedChainConstraintConstraint()));

			} else if (((LineConnection) getModel())
					.getFutureUnwantedChainConstraintConstraint().isEmpty()
					&& (!((LineConnection) getModel())
							.getFutureUnwantedChainConstraintReset().isEmpty())) {
				endPointLabel = new UnwantedChainConstraintFigure(
						temp,
						ForShort(((LineConnection) getModel())
								.getFutureUnwantedChainConstraintValue())
								+ ";"
								+ ForShort(((LineConnection) getModel())
										.getFutureUnwantedChainConstraintReset()));

			} else if ((!((LineConnection) getModel())
					.getFutureUnwantedChainConstraintConstraint().isEmpty())
					&& (!((LineConnection) getModel())
							.getFutureUnwantedChainConstraintReset().isEmpty())) {
				endPointLabel = new UnwantedChainConstraintFigure(
						temp,
						ForShort(((LineConnection) getModel())
								.getFutureUnwantedChainConstraintValue())
								+ ";"
								+ ForShort(((LineConnection) getModel())
										.getFutureUnwantedChainConstraintConstraint())
								+ ";"
								+ ForShort(((LineConnection) getModel())
										.getFutureUnwantedChainConstraintReset()));
			}
			// ConnectionEndpointLocator endpointLocator = new
			// ConnectionEndpointLocator(
			// ((PolylineConnection) getFigure()), true);
			// if (temp) {
			// endpointLocator.setUDistance(-36);
			// endpointLocator.setVDistance(-60);
			// } else {
			// endpointLocator.setUDistance(-36);
			// }
			// ((PolylineConnection) getFigure()).add(endPointLabel,
			// endpointLocator);
			endPointLabel.setSize(80, 50);
			((PolylineConnection) getFigure()).add(endPointLabel,
					new TargetPointOffsetLocator(
							((PolylineConnection) getFigure()), 0, new Point(0,
									23)));
			
		}
	}

	/**
	 * @param presentConstraintValue
	 * @return
	 */
	private String ForShort(String presentConstraintValue) {
		// TODO Auto-generated method stub
		if (!presentConstraintValue.contains("<=")
				&& !presentConstraintValue.contains(">=")
				&& !presentConstraintValue.contains(":=")) {
			int index = presentConstraintValue.indexOf('=');
			if (index >= 1) {
				return presentConstraintValue.substring(0, index);
			}
		}
		return presentConstraintValue;
	}

}
