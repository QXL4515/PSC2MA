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
 * Package: cn.cstv.wspscm.model                                            
 * File: LineConnection.java                                                   
 * Program: LineConnection                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-7-22                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.model;



import org.eclipse.draw2d.geometry.Point;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * @author Su Zhiyong & Zhang Pengcheng
 * 
 */
public class LineConnection extends AbstractConnection {
	public LineConnection() {
		super();
	}

	public LineConnection(Lifeline source, Lifeline target) {
		super(source, target);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean isStrict = false;
	private Integer isStrict_int = 0;
	private Point location = new Point(0, 0);
	private Integer messageType = 0;
	private String messageValue = "";
	private String messagePara = "";
//	private String messageReset = "";
	
	private String PresentConstraintValue = "";
	private String PresentConstraintConstraint = "";
	private String PresentConstraintReset = "";

	private String PastUnwantedMessageConstraintValue = "";
	private String PastUnwantedMessageConstraintConstraint = "";
	private String PastUnwantedMessageConstraintReset = "";

	private String FutureUnwantedMessageConstraintValue = "";
	private String FutureUnwantedMessageConstraintConstraint= "";
	private String FutureUnwantedMessageConstraintReset = "";
	
	/*
 	private String PastBooleanConstraintValue = "";
	private String PastBooleanConstraintConstraint = "";
	private String PastBooleanConstraintReset = "";

	private String FutureBooleanConstraintValue = "";
	private String FutureBooleanConstraintConstraint = "";
	private String FutureBooleanConstraintReset = "";
	 * */

	private String PresentPastValue = "";
	private String PresentPastConstraint = "";
	private String PresentPastReset = "";

	private String PresentFutureValue = "";
	private String PresentFutureConstraint = "";
	private String PresentFutureReset = "";

	private String PastWantedChainConstraintValue = "";
	private String PastWantedChainConstraintConstraint = "";
	private String PastWantedChainConstraintReset = "";

	private String FutureWantedChainConstraintValue = "";
	private String FutureWantedChainConstraintConstraint = "";
	private String FutureWantedChainConstraintReset = "";

	private String PastUnwantedChainConstraintValue = "";
	private String PastUnwantedChainConstraintConstraint = "";
	private String PastUnwantedChainConstraintReset = "";

	private String FutureUnwantedChainConstraintValue = "";
	private String FutureUnwantedChainConstraintConstraint = "";
	private String FutureUnwantedChainConstraintReset = "";

	public static final String P_LOCATION = "_location";
	public static final String P_ISSTRICT = "_isstrict";
	public static final String P_MESSAGE_TYPE = "_message_type";
	public static final String P_MESSAGE_VALUE = "_message_value";
	public static final String P_MESSAGE_PARA = "_message_para";
//	public static final String P_MESSAGE_RESET = "_message_reset";

	public static final String P_PRESENTCONSTRAINT_VALUE = "_PresentConstraint_value";
	public static final String P_PRESENTCONSTRAINT_CONSTRAINT = "_PresentConstraint_constraint";
	public static final String P_PRESENTCONSTRAINT_RESET = "_PresentConstraint_reset";

	/*
	 * 
	public static final String P_PASTBOOLEANCONSTRAINT_VALUE = "_PastBooleanConstraint_value";
	public static final String P_PASTBOOLEANCONSTRAINT_CONSTRAINT = "_PastBooleanConstraint_constraint";
	public static final String P_PASTBOOLEANCONSTRAINT_RESET = "_PastBooleanConstraint_reset";

	public static final String P_FUTUREBOOLEANCONSTRAINT_VALUE = "_FutureBooleanConstraint_value";
	public static final String P_FUTUREBOOLEANCONSTRAINT_CONSTRAINT = "_FutureBooleanConstraint_constraint";
	public static final String P_FUTUREBOOLEANCONSTRAINT_RESET = "_FutureBooleanConstraint_reset";
	 * */
	
	public static final String P_PRESENTPAST_VALUE = "_PresentPast_value";
	public static final String P_PRESENTPAST_CONSTRAINT = "_PresentPast_constraint";
	public static final String P_PRESENTPAST_RESET = "_PresentPast_reset";

	public static final String P_PRESENTFUTURE_VALUE = "_PresentFuture_value";
	public static final String P_PRESENTFUTURE_CONSTRAINT = "_PresentFuture_constraint";
	public static final String P_PRESENTFUTURE_RESET = "_PresentFuture_reset";

	public static final String P_PASTWANTEDCHAINCONSTRAINT_VALUE = "_PastWantedChainConstraintt_value";
	public static final String P_PASTWANTEDCHAINCONSTRAINT_CONSTRAINT = "_PastWantedChainConstraintt_constraint";
	public static final String P_PASTWANTEDCHAINCONSTRAINT_RESET = "_PastWantedChainConstraintt_reset";
		  
	public static final String P_FUTUREWANTEDCHAINCONSTRAINT_VALUE = "_FutureWantedChainConstraint_value";
	public static final String P_FUTUREWANTEDCHAINCONSTRAINT_CONSTRAINT = "_FutureWantedChainConstraint_constraint";
	public static final String P_FUTUREWANTEDCHAINCONSTRAINT_RESET = "_FutureWantedChainConstraint_reset";
	
	public static final String P_PASTUNWANTEDCHAINCONSTRAINT_VALUE = "_PastUnwantedChainConstraint_value";
	public static final String P_PASTUNWANTEDCHAINCONSTRAINT_CONSTRAINT = "_PastUnwantedChainConstraint_constraint";
	public static final String P_PASTUNWANTEDCHAINCONSTRAINT_RESET = "_PastUnwantedChainConstraint_reset";

	public static final String P_FUTUREUNWANTEDCHAINCONSTRAINT_VALUE = "_FutureUnwantedChainConstraint_value";
	public static final String P_FUTUREUNWANTEDCHAINCONSTRAINT_CONSTRAINT = "_FutureUnwantedChainConstraint_constraint";
	public static final String P_FUTUREUNWANTEDCHAINCONSTRAINT_RESET = "_FutureUnwantedChainConstraint_reset";
	
	public static final String P_PASTUNWANTEDMESSAGECONSTRAINT_VALUE = "_PastUnwantedMessageConstraintt_value";
	public static final String P_PASTUNWANTEDMESSAGECONSTRAINT_CONSTRAINT = "_PastUnwantedMessageConstraintt_constraint";
	public static final String P_PASTUNWANTEDMESSAGECONSTRAINT_RESET = "_PastUnwantedMessageConstraintt_reset";
		  
	public static final String P_FUTUREUNWANTEDMESSAGECONSTRAINT_VALUE = "_FutureUnwantedMessageConstraint_value";
	public static final String P_FUTUREUNWANTEDMESSAGECONSTRAINT_CONSTRAINT = "_FutureUnwantedMessageConstraint_constraint";
	public static final String P_FUTUREUNWANTEDMESSAGECONSTRAINT_RESET = "_FutureUnwWantedMessageConstraint_reset";
	
	private String typesArray [] = {"e:", "r:", "f:" };
	private String strictArray [] = {"false", "true"};
	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		PropertyDescriptor messagePropertyDescriptorType = new ComboBoxPropertyDescriptor(
				P_MESSAGE_TYPE, "Type", typesArray);
		PropertyDescriptor messagePropertyDescriptorValue = new TextPropertyDescriptor(
				P_MESSAGE_VALUE, "Value");
		PropertyDescriptor messagePropertyDescriptorPara = new TextPropertyDescriptor(
				P_MESSAGE_PARA, "Para");
//		PropertyDescriptor messagePropertyDescriptorReset = new TextPropertyDescriptor(
//				P_MESSAGE_RESET, "Reset");
		messagePropertyDescriptorType.setCategory("Message");
		messagePropertyDescriptorValue.setCategory("Message");
		messagePropertyDescriptorPara.setCategory("Message");
//		messagePropertyDescriptorReset.setCategory("Message");
		
		PropertyDescriptor strict = new ComboBoxPropertyDescriptor(
				P_ISSTRICT, "Strict", strictArray);
		strict.setCategory("Strict");
		
		PropertyDescriptor pastWantedChainConstraintPropertyDescriptorValue = new TextPropertyDescriptor(
				P_PASTWANTEDCHAINCONSTRAINT_VALUE, "Value");
		PropertyDescriptor pastWantedChainConstraintPropertyDescriptorConstraint = new TextPropertyDescriptor(
				P_PASTWANTEDCHAINCONSTRAINT_CONSTRAINT, "Constraint");
		PropertyDescriptor pastWantedChainConstraintPropertyDescriptorReset = new TextPropertyDescriptor(
				P_PASTWANTEDCHAINCONSTRAINT_RESET, "Reset");
		pastWantedChainConstraintPropertyDescriptorValue.setCategory("PastWantedChainConstraint");
		pastWantedChainConstraintPropertyDescriptorConstraint.setCategory("PastWantedChainConstraint");
		pastWantedChainConstraintPropertyDescriptorReset.setCategory("PastWantedChainConstraint");

		PropertyDescriptor futureWantedChainConstraintPropertyDescriptorValue = new TextPropertyDescriptor(
				P_FUTUREWANTEDCHAINCONSTRAINT_VALUE, "Value");
		PropertyDescriptor futureWantedChainConstraintPropertyDescriptorConstraint = new TextPropertyDescriptor(
				P_FUTUREWANTEDCHAINCONSTRAINT_CONSTRAINT, "Constraint");
		PropertyDescriptor futureWantedChainConstraintPropertyDescriptorReset = new TextPropertyDescriptor(
				P_FUTUREWANTEDCHAINCONSTRAINT_RESET, "Reset");
		futureWantedChainConstraintPropertyDescriptorValue.setCategory("FutureWantedChainConstraint");
		futureWantedChainConstraintPropertyDescriptorConstraint.setCategory("FutureWantedChainConstraint");
		futureWantedChainConstraintPropertyDescriptorReset.setCategory("FutureWantedChainConstraint");

		PropertyDescriptor pastUnwantedChainConstraintPropertyDescriptorValue = new TextPropertyDescriptor(
				P_PASTUNWANTEDCHAINCONSTRAINT_VALUE, "Value");
		PropertyDescriptor pastUnwantedChainConstraintPropertyDescriptorConstraint = new TextPropertyDescriptor(
				P_PASTUNWANTEDCHAINCONSTRAINT_CONSTRAINT, "Constraint");
		PropertyDescriptor pastUnwantedChainConstraintPropertyDescriptorReset = new TextPropertyDescriptor(
				P_PASTUNWANTEDCHAINCONSTRAINT_RESET, "Reset");
		pastUnwantedChainConstraintPropertyDescriptorValue.setCategory("PastUnwantedChainConstraint");
		pastUnwantedChainConstraintPropertyDescriptorConstraint.setCategory("PastUnwantedChainConstraint");
		pastUnwantedChainConstraintPropertyDescriptorReset.setCategory("PastUnwantedChainConstraint");

		PropertyDescriptor futureUnwantedChainConstraintPropertyDescriptorValue = new TextPropertyDescriptor(
				P_FUTUREUNWANTEDCHAINCONSTRAINT_VALUE, "Value");
		PropertyDescriptor futureUnwantedChainConstraintPropertyDescriptorConstraint = new TextPropertyDescriptor(
				P_FUTUREUNWANTEDCHAINCONSTRAINT_CONSTRAINT, "Constraint");
		PropertyDescriptor futureUnwantedChainConstraintPropertyDescriptorReset = new TextPropertyDescriptor(
				P_FUTUREUNWANTEDCHAINCONSTRAINT_RESET, "Reset");
		futureUnwantedChainConstraintPropertyDescriptorValue.setCategory("FutureUnwantedChainConstraint");
		futureUnwantedChainConstraintPropertyDescriptorConstraint.setCategory("FutureUnwantedChainConstraint");
		futureUnwantedChainConstraintPropertyDescriptorReset.setCategory("FutureUnwantedChainConstraint");

		PropertyDescriptor presentConstraintPropertyDescriptorValue = new TextPropertyDescriptor(
				P_PRESENTCONSTRAINT_VALUE, "Value");
		PropertyDescriptor presentConstraintPropertyDescriptorConstraint = new TextPropertyDescriptor(
				P_PRESENTCONSTRAINT_CONSTRAINT, "Constraint");
		PropertyDescriptor presentConstraintPropertyDescriptorReset = new TextPropertyDescriptor(
				P_PRESENTCONSTRAINT_RESET, "Reset");
		presentConstraintPropertyDescriptorValue.setCategory("PresentConstraint");
		presentConstraintPropertyDescriptorConstraint.setCategory("PresentConstraint");
		presentConstraintPropertyDescriptorReset.setCategory("PresentConstraint");
		
		
		
		
		PropertyDescriptor pastUnwantedMessageConstraintPropertyDescriptorValue = new TextPropertyDescriptor(
				P_PASTUNWANTEDMESSAGECONSTRAINT_VALUE, "Value");
		PropertyDescriptor pastUnwantedMessageConstraintPropertyDescriptorConstraint = new TextPropertyDescriptor(
				P_PASTUNWANTEDMESSAGECONSTRAINT_CONSTRAINT, "Constraint");
		PropertyDescriptor pastUnwantedMessageConstraintPropertyDescriptorReset = new TextPropertyDescriptor(
				P_PASTUNWANTEDMESSAGECONSTRAINT_RESET, "Reset");
		pastUnwantedMessageConstraintPropertyDescriptorValue.setCategory("PastUnwantedMessageConstraint");
		pastUnwantedMessageConstraintPropertyDescriptorConstraint.setCategory("PastUnwantedMessageConstraint");
		pastUnwantedMessageConstraintPropertyDescriptorReset.setCategory("PastUnwantedMessageConstraint");

		PropertyDescriptor futureUnwantedMessageConstraintPropertyDescriptorValue = new TextPropertyDescriptor(
				P_FUTUREUNWANTEDMESSAGECONSTRAINT_VALUE, "Value");
		PropertyDescriptor futureUnwantedMessageConstraintPropertyDescriptorConstraint = new TextPropertyDescriptor(
				P_FUTUREUNWANTEDMESSAGECONSTRAINT_CONSTRAINT, "Constraint");
		PropertyDescriptor futureUnwantedMessageConstraintPropertyDescriptorReset = new TextPropertyDescriptor(
				P_FUTUREUNWANTEDMESSAGECONSTRAINT_RESET, "Reset");
		futureUnwantedMessageConstraintPropertyDescriptorValue.setCategory("FutureUnwantedMessageConstraint");
		futureUnwantedMessageConstraintPropertyDescriptorConstraint.setCategory("FutureUnwantedMessageConstraint");
		futureUnwantedMessageConstraintPropertyDescriptorReset.setCategory("FutureUnwantedMessageConstraint");

		/*
		 PropertyDescriptor pastBooleanConstraintPropertyDescriptorValue = new TextPropertyDescriptor(
				P_PASTBOOLEANCONSTRAINT_VALUE, "Value");
		PropertyDescriptor pastBooleanConstraintPropertyDescriptorConstraint = new TextPropertyDescriptor(
				P_PASTBOOLEANCONSTRAINT_CONSTRAINT, "Constraint");
		PropertyDescriptor pastBooleanConstraintPropertyDescriptorReset = new TextPropertyDescriptor(
				P_PASTBOOLEANCONSTRAINT_RESET, "Reset");
		pastBooleanConstraintPropertyDescriptorValue.setCategory("PastBooleanConstraint");
		pastBooleanConstraintPropertyDescriptorConstraint.setCategory("PastBooleanConstraint");
		pastBooleanConstraintPropertyDescriptorReset.setCategory("PastBooleanConstraint");

		PropertyDescriptor futureBooleanConstraintPropertyDescriptorValue = new TextPropertyDescriptor(
				P_FUTUREBOOLEANCONSTRAINT_VALUE, "Value");
		PropertyDescriptor futureBooleanConstraintPropertyDescriptorConstraint = new TextPropertyDescriptor(
				P_FUTUREBOOLEANCONSTRAINT_CONSTRAINT, "Constraint");
		PropertyDescriptor futureBooleanConstraintPropertyDescriptorReset = new TextPropertyDescriptor(
				P_FUTUREBOOLEANCONSTRAINT_RESET, "Reset");
		futureBooleanConstraintPropertyDescriptorValue.setCategory("FutureBooleanConstraint");
		futureBooleanConstraintPropertyDescriptorConstraint.setCategory("FutureBooleanConstraint");
		futureBooleanConstraintPropertyDescriptorReset.setCategory("FutureBooleanConstraint");
		 */

		PropertyDescriptor presentPastPropertyDescriptorValue = new TextPropertyDescriptor(
				P_PRESENTPAST_VALUE, "Value");
		PropertyDescriptor presentPastPropertyDescriptorConstraint = new TextPropertyDescriptor(
				P_PRESENTPAST_CONSTRAINT, "Constraint");
		PropertyDescriptor presentPastPropertyDescriptorReset = new TextPropertyDescriptor(
				P_PRESENTPAST_RESET, "Reset");
		presentPastPropertyDescriptorValue.setCategory("PresentPast");
		presentPastPropertyDescriptorConstraint.setCategory("PresentPast");
		presentPastPropertyDescriptorReset.setCategory("PresentPast");

		PropertyDescriptor presentFuturePropertyDescriptorValue = new TextPropertyDescriptor(
				P_PRESENTFUTURE_VALUE, "Value");
		PropertyDescriptor presentFuturePropertyDescriptorConstraint = new TextPropertyDescriptor(
				P_PRESENTFUTURE_CONSTRAINT, "Constraint");
		PropertyDescriptor presentFuturePropertyDescriptorReset = new TextPropertyDescriptor(
				P_PRESENTFUTURE_RESET, "Reset");
		presentFuturePropertyDescriptorValue.setCategory("PresentFuture");
		presentFuturePropertyDescriptorConstraint.setCategory("PresentFuture");
		presentFuturePropertyDescriptorReset.setCategory("PresentFuture");

		


		IPropertyDescriptor[] descriptors = new IPropertyDescriptor[] {
				messagePropertyDescriptorType,
				messagePropertyDescriptorValue,
				messagePropertyDescriptorPara,
//				messagePropertyDescriptorReset,
				strict,
				
				pastUnwantedMessageConstraintPropertyDescriptorValue,
				pastUnwantedMessageConstraintPropertyDescriptorConstraint,
				pastUnwantedMessageConstraintPropertyDescriptorReset,

				futureUnwantedMessageConstraintPropertyDescriptorValue,
				futureUnwantedMessageConstraintPropertyDescriptorConstraint,
				futureUnwantedMessageConstraintPropertyDescriptorReset,
				
				pastWantedChainConstraintPropertyDescriptorValue,
				pastWantedChainConstraintPropertyDescriptorConstraint,
				pastWantedChainConstraintPropertyDescriptorReset,

				futureWantedChainConstraintPropertyDescriptorValue,
				futureWantedChainConstraintPropertyDescriptorConstraint,
				futureWantedChainConstraintPropertyDescriptorReset,

				pastUnwantedChainConstraintPropertyDescriptorValue,
				pastUnwantedChainConstraintPropertyDescriptorConstraint,
				pastUnwantedChainConstraintPropertyDescriptorReset,

				futureUnwantedChainConstraintPropertyDescriptorValue,
				futureUnwantedChainConstraintPropertyDescriptorConstraint,
				futureUnwantedChainConstraintPropertyDescriptorReset,

				presentConstraintPropertyDescriptorValue,
				presentConstraintPropertyDescriptorConstraint,
				presentConstraintPropertyDescriptorReset,

				

				presentPastPropertyDescriptorValue,
				presentPastPropertyDescriptorConstraint,
				presentPastPropertyDescriptorReset,

				presentFuturePropertyDescriptorValue,
				presentFuturePropertyDescriptorConstraint,
				presentFuturePropertyDescriptorReset};
		return descriptors;
	}

	@Override
	public Object getPropertyValue(Object id) {
		// TODO Auto-generated method stub
		if (id.equals(P_MESSAGE_VALUE)) {
			return getMessageValue();
		} else if (id.equals(P_MESSAGE_PARA)) {
			return getMessagePara();
//		} else if (id.equals(P_MESSAGE_RESET)) {
//			return getMessageReset();
		} else if (id.equals(P_MESSAGE_TYPE)) {
			return getMessageType();
		} else if(id.equals(P_ISSTRICT)){
			return getIsStrict_int();
		}
		
		else if (id.equals(P_PRESENTCONSTRAINT_VALUE)) {
			return getPresentConstraintValue();
		} else if (id.equals(P_PRESENTCONSTRAINT_CONSTRAINT)) {
			return getPresentConstraintConstraint();
		} else if (id.equals(P_PRESENTCONSTRAINT_RESET)) {
			return getPresentConstraintReset();
		} 
		
		
		else if (id.equals(P_PASTUNWANTEDMESSAGECONSTRAINT_VALUE)) {
			return getPastUnwantedMessageConstraintValue();
		} else if (id.equals(P_PASTUNWANTEDMESSAGECONSTRAINT_CONSTRAINT)) {
			return getPastUnwantedMessageConstraintConstraint();
		} else if (id.equals(P_PASTUNWANTEDMESSAGECONSTRAINT_RESET)) {
			return getPastUnwantedMessageConstraintReset();
		} 
	
		else if (id.equals(P_FUTUREUNWANTEDMESSAGECONSTRAINT_VALUE)) {
			return getFutureUnwantedMessageConstraintValue();
		} else if (id.equals(P_FUTUREUNWANTEDMESSAGECONSTRAINT_CONSTRAINT)) {
			return getFutureUnwantedMessageConstraintConstraint();
		} else if (id.equals(P_FUTUREUNWANTEDMESSAGECONSTRAINT_RESET)) {
			return getFutureUnwantedMessageConstraintReset();
		} 
		
		/*
		 * else if (id.equals(P_PASTBOOLEANCONSTRAINT_VALUE)) {
			return getPastBooleanConstraintValue();
		} else if (id.equals(P_PASTBOOLEANCONSTRAINT_CONSTRAINT)) {
			return getPastBooleanConstraintConstraint();
		} else if (id.equals(P_PASTBOOLEANCONSTRAINT_RESET)) {
			return getPastBooleanConstraintReset();
		} 
		
		
			else if (id.equals(P_FUTUREBOOLEANCONSTRAINT_VALUE)) {
			return getFutureBooleanConstraintValue();
		} else if (id.equals(P_FUTUREBOOLEANCONSTRAINT_CONSTRAINT)) {
			return getFutureBooleanConstraintConstraint();
		} else if (id.equals(P_FUTUREBOOLEANCONSTRAINT_RESET)) {
			return getFutureBooleanConstraintReset();
		} 
		*/
		
		else if (id.equals(P_PRESENTPAST_VALUE)) {
			return getPresentPastValue();
		} else if (id.equals(P_PRESENTPAST_CONSTRAINT)) {
			return getPresentPastConstraint();
		} else if (id.equals(P_PRESENTPAST_RESET)) {
			return getPresentPastReset();
		} 

		else if (id.equals(P_PRESENTFUTURE_VALUE)) {
			return getPresentFutureValue();
		} else if (id.equals(P_PRESENTFUTURE_CONSTRAINT)) {
			return getPresentFutureConstraint();
		} else if (id.equals(P_PRESENTFUTURE_RESET)) {
			return getPresentFutureReset();
		} 

		else if (id.equals(P_PASTWANTEDCHAINCONSTRAINT_VALUE)) {
			return getPastWantedChainConstraintValue();
		} else if (id.equals(P_PASTWANTEDCHAINCONSTRAINT_CONSTRAINT)) {
			return getPastWantedChainConstraintConstraint();
		} else if (id.equals(P_PASTWANTEDCHAINCONSTRAINT_RESET)) {
			return getPastWantedChainConstraintReset();
		} 

		else if (id.equals(P_FUTUREWANTEDCHAINCONSTRAINT_VALUE)) {
			return getFutureWantedChainConstraintValue();
		} else if (id.equals(P_FUTUREWANTEDCHAINCONSTRAINT_CONSTRAINT)) {
			return getFutureWantedChainConstraintConstraint();
		} else if (id.equals(P_FUTUREWANTEDCHAINCONSTRAINT_RESET)) {
			return getFutureWantedChainConstraintReset();
		} 

		else if (id.equals(P_PASTUNWANTEDCHAINCONSTRAINT_VALUE)) {
			return getPastUnwantedChainConstraintValue();
		} else if (id.equals(P_PASTUNWANTEDCHAINCONSTRAINT_CONSTRAINT)) {
			return getPastUnwantedChainConstraintConstraint();
		} else if (id.equals(P_PASTUNWANTEDCHAINCONSTRAINT_RESET)) {
			return getPastUnwantedChainConstraintReset();
		} 

		else if (id.equals(P_FUTUREUNWANTEDCHAINCONSTRAINT_VALUE)) {
			return getFutureUnwantedChainConstraintValue();
		} else if (id.equals(P_FUTUREUNWANTEDCHAINCONSTRAINT_CONSTRAINT)) {
			return getFutureUnwantedChainConstraintConstraint();
		} else if (id.equals(P_FUTUREUNWANTEDCHAINCONSTRAINT_RESET)) {
			return getFutureUnwantedChainConstraintReset();
		} 

		return super.getPropertyValue(id);
	}

	@Override
	public boolean isPropertySet(Object id) {
		// TODO Auto-generated method stub
		if (id.equals(P_MESSAGE_VALUE)) {
			return true;
		} else if (id.equals(P_MESSAGE_PARA)) {
			return true;
//		} else if (id.equals(P_MESSAGE_RESET)) {
//			return true;
		} else if (id.equals(P_MESSAGE_TYPE)) {
			return true;
		} else if (id.equals(P_ISSTRICT)){
			return true;
		}
		
		else if (id.equals(P_PRESENTCONSTRAINT_VALUE)) {
			return true;
		} else if (id.equals(P_PRESENTCONSTRAINT_CONSTRAINT)) {
			return true;
		} else if (id.equals(P_PRESENTCONSTRAINT_RESET)) {
			return true;
		} 
		

		
		else if (id.equals(P_PASTUNWANTEDMESSAGECONSTRAINT_VALUE)) {
			return true;
		} else if (id.equals(P_PASTUNWANTEDMESSAGECONSTRAINT_CONSTRAINT)) {
			return true;
		} else if (id.equals(P_PASTUNWANTEDMESSAGECONSTRAINT_RESET)) {
			return true;
		} 
		
		else if (id.equals(P_FUTUREUNWANTEDMESSAGECONSTRAINT_VALUE)) {
			return true;
		} else if (id.equals(P_FUTUREUNWANTEDMESSAGECONSTRAINT_CONSTRAINT)) {
			return true;
		} else if (id.equals(P_FUTUREUNWANTEDMESSAGECONSTRAINT_RESET)) {
			return true;
		} 
		
		/*
		 * 	else if (id.equals(P_PASTBOOLEANCONSTRAINT_VALUE)) {
			return true;
		} else if (id.equals(P_PASTBOOLEANCONSTRAINT_CONSTRAINT)) {
			return true;
		} else if (id.equals(P_PASTBOOLEANCONSTRAINT_RESET)) {
			return true;
		} 
		
		else if (id.equals(P_FUTUREBOOLEANCONSTRAINT_VALUE)) {
			return true;
		} else if (id.equals(P_FUTUREBOOLEANCONSTRAINT_CONSTRAINT)) {
			return true;
		} else if (id.equals(P_FUTUREBOOLEANCONSTRAINT_RESET)) {
			return true;
		} 
		 * */
		
		else if (id.equals(P_PRESENTPAST_VALUE)) {
			return true;
		} else if (id.equals(P_PRESENTPAST_CONSTRAINT)) {
			return true;
		} else if (id.equals(P_PRESENTPAST_RESET)) {
			return true;
		} 

		else if (id.equals(P_PRESENTFUTURE_VALUE)) {
			return true;
		} else if (id.equals(P_PRESENTFUTURE_CONSTRAINT)) {
			return true;
		} else if (id.equals(P_PRESENTFUTURE_RESET)) {
			return true;
		} 

		else if (id.equals(P_PASTWANTEDCHAINCONSTRAINT_VALUE)) {
			return true;
		} else if (id.equals(P_PASTWANTEDCHAINCONSTRAINT_CONSTRAINT)) {
			return true;
		} else if (id.equals(P_PASTWANTEDCHAINCONSTRAINT_RESET)) {
			return true;
		} 

		else if (id.equals(P_FUTUREWANTEDCHAINCONSTRAINT_VALUE)) {
			return true;
		} else if (id.equals(P_FUTUREWANTEDCHAINCONSTRAINT_CONSTRAINT)) {
			return true;
		} else if (id.equals(P_FUTUREWANTEDCHAINCONSTRAINT_RESET)) {
			return true;
		} 

		else if (id.equals(P_PASTUNWANTEDCHAINCONSTRAINT_VALUE)) {
			return true;
		} else if (id.equals(P_PASTUNWANTEDCHAINCONSTRAINT_CONSTRAINT)) {
			return true;
		} else if (id.equals(P_PASTUNWANTEDCHAINCONSTRAINT_RESET)) {
			return true;
		} 

		else if (id.equals(P_FUTUREUNWANTEDCHAINCONSTRAINT_VALUE)) {
			return true;
		} else if (id.equals(P_FUTUREUNWANTEDCHAINCONSTRAINT_CONSTRAINT)) {
			return true;
		} else if (id.equals(P_FUTUREUNWANTEDCHAINCONSTRAINT_RESET)) {
			return true;
		}  else {
			return false;
		}
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		// TODO Auto-generated method stub
		if (id.equals(P_MESSAGE_VALUE)) {
			setMessageValue((String) value);
		} else if (id.equals(P_MESSAGE_PARA)) {
			setMessagePara((String) value);
//		} else if (id.equals(P_MESSAGE_RESET)) {
//			setMessageReset((String) value);
		} else if (id.equals(P_MESSAGE_TYPE)) {
			setMessageType((Integer) value);
		} else if (id.equals(P_ISSTRICT)){
			setIsStrict_int((Integer) value);
		}
		
		
		else if (id.equals(P_PRESENTCONSTRAINT_VALUE)) {
			setPresentConstraintValue((String) value);
		} else if (id.equals(P_PRESENTCONSTRAINT_CONSTRAINT)) {
			setPresentConstraintConstraint((String) value);
		} else if (id.equals(P_PRESENTCONSTRAINT_RESET)) {
			setPresentConstraintReset((String) value);
		} 
		
		
		else if (id.equals(P_PASTUNWANTEDMESSAGECONSTRAINT_VALUE)) {
			 setPastUnwantedMessageConstraintValue((String) value);
		} else if (id.equals(P_PASTUNWANTEDMESSAGECONSTRAINT_CONSTRAINT)) {
			 setPastUnwantedMessageConstraintConstraint((String) value);
		} else if (id.equals(P_PASTUNWANTEDMESSAGECONSTRAINT_RESET)) {
			 setPastUnwantedMessageConstraintReset((String) value);
		} 
	
		else if (id.equals(P_FUTUREUNWANTEDMESSAGECONSTRAINT_VALUE)) {
			 setFutureUnwantedMessageConstraintValue((String) value);
		} else if (id.equals(P_FUTUREUNWANTEDMESSAGECONSTRAINT_CONSTRAINT)) {
			 setFutureUnwantedMessageConstraintConstraint((String) value);
		} else if (id.equals(P_FUTUREUNWANTEDMESSAGECONSTRAINT_RESET)) {
			 setFutureUnwantedMessageConstraintReset((String) value);
		} 
		
		/*
		 * else if (id.equals(P_PASTBOOLEANCONSTRAINT_VALUE)) {
			setPastBooleanConstraintValue((String) value);
		} else if (id.equals(P_PASTBOOLEANCONSTRAINT_CONSTRAINT)) {
			setPastBooleanConstraintConstraint((String) value);
		} else if (id.equals(P_PASTBOOLEANCONSTRAINT_RESET)) {
			setPastBooleanConstraintReset((String) value);
		} 
		
		else if (id.equals(P_FUTUREBOOLEANCONSTRAINT_VALUE)) {
			setFutureBooleanConstraintValue((String) value);
		} else if (id.equals(P_FUTUREBOOLEANCONSTRAINT_CONSTRAINT)) {
			setFutureBooleanConstraintConstraint((String) value);
		} else if (id.equals(P_FUTUREBOOLEANCONSTRAINT_RESET)) {
			setFutureBooleanConstraintReset((String) value);
		} 
		 * */
		
		else if (id.equals(P_PRESENTPAST_VALUE)) {
			setPresentPastValue((String) value);
		} else if (id.equals(P_PRESENTPAST_CONSTRAINT)) {
			setPresentPastConstraint((String) value);
		} else if (id.equals(P_PRESENTPAST_RESET)) {
			setPresentPastReset((String) value);
		} 

		else if (id.equals(P_PRESENTFUTURE_VALUE)) {
			setPresentFutureValue((String) value);
		} else if (id.equals(P_PRESENTFUTURE_CONSTRAINT)) {
			setPresentFutureConstraint((String) value);
		} else if (id.equals(P_PRESENTFUTURE_RESET)) {
			setPresentFutureReset((String) value);
		} 

		else if (id.equals(P_PASTWANTEDCHAINCONSTRAINT_VALUE)) {
			setPastWantedChainConstraintValue((String) value);
		} else if (id.equals(P_PASTWANTEDCHAINCONSTRAINT_CONSTRAINT)) {
			setPastWantedChainConstraintConstraint((String) value);
		} else if (id.equals(P_PASTWANTEDCHAINCONSTRAINT_RESET)) {
			setPastWantedChainConstraintReset((String) value);
		} 

		else if (id.equals(P_FUTUREWANTEDCHAINCONSTRAINT_VALUE)) {
			setFutureWantedChainConstraintValue((String) value);
		} else if (id.equals(P_FUTUREWANTEDCHAINCONSTRAINT_CONSTRAINT)) {
			setFutureWantedChainConstraintConstraint((String) value);
		} else if (id.equals(P_FUTUREWANTEDCHAINCONSTRAINT_RESET)) {
			setFutureWantedChainConstraintReset((String) value);
		} 

		else if (id.equals(P_PASTUNWANTEDCHAINCONSTRAINT_VALUE)) {
			setPastUnwantedChainConstraintValue((String) value);
		} else if (id.equals(P_PASTUNWANTEDCHAINCONSTRAINT_CONSTRAINT)) {
			setPastUnwantedChainConstraintConstraint((String) value);
		} else if (id.equals(P_PASTUNWANTEDCHAINCONSTRAINT_RESET)) {
			setPastUnwantedChainConstraintReset((String) value);
		} 

		else if (id.equals(P_FUTUREUNWANTEDCHAINCONSTRAINT_VALUE)) {
			setFutureUnwantedChainConstraintValue((String) value);
		} else if (id.equals(P_FUTUREUNWANTEDCHAINCONSTRAINT_CONSTRAINT)) {
			setFutureUnwantedChainConstraintConstraint((String) value);
		} else if (id.equals(P_FUTUREUNWANTEDCHAINCONSTRAINT_RESET)) {
			setFutureUnwantedChainConstraintReset((String) value);
		} 
	}

	
	public boolean isStrict() {
		return isStrict;
	}
	public void setStrict(boolean isStrict) {
		this.isStrict = isStrict;
		firePropertyChange(P_ISSTRICT, null, this.isStrict);
	}
	
	public Integer getIsStrict_int(){											//*********************���ܴ���bug
		return isStrict_int;
	}
	public void setIsStrict_int(Integer newType){								//*********************���ܴ���bug
		if(isStrict_int != newType){
			this.isStrict_int = newType;
			if(isStrict==false){
				isStrict = true;
			}else{
				isStrict = false;
			}
		}
		firePropertyChange(P_ISSTRICT, null, this.isStrict_int);
	}


	/**
	 * @return the messageValue
	 */
	public String getMessageValue() {
		return messageValue;
	}

	/**
	 * @param messageValue
	 *            the messageValue to set
	 */
	public void setMessageValue(String messageValue) {
		this.messageValue = messageValue;
		firePropertyChange(P_MESSAGE_VALUE, null, this.messageValue);

	}
	
	public Integer getMessageType(){											//*********************���ܴ���bug
		return messageType;
	}
	
	public void setMessageType(Integer newType){								//*********************���ܴ���bug
		if(messageType != newType){
			this.messageType = newType;
		}
		firePropertyChange(P_MESSAGE_TYPE, null, this.messageType);
	}
	/**
	 * @return the messageConstraint
	 */
	public String getMessagePara() {
		return messagePara;
	}

	/**
	 * @param messageConstraint
	 *            the messageConstraint to set
	 */
	public void setMessagePara(String messagePara) {
		this.messagePara = messagePara;
		firePropertyChange(P_MESSAGE_PARA, null, this.messagePara);
	}

	/**
	 * @return the messageReset
	 */
///	public String getMessageReset() {
//		return messageReset;
//	}

	/**
	 * @param messageReset
	 *            the messageReset to set
	 */
//	public void setMessageReset(String messageReset) {
//		this.messageReset = messageReset;
//		firePropertyChange(P_MESSAGE_RESET, null, this.messageReset);
//	}

	/**
	 * @return the presentConstraintValue
	 */
	public String getPresentConstraintValue() {
		return PresentConstraintValue;
	}

	/**
	 * @param presentConstraintValue the presentConstraintValue to set
	 */
	public void setPresentConstraintValue(String presentConstraintValue) {
		PresentConstraintValue = presentConstraintValue;
		firePropertyChange(P_PRESENTCONSTRAINT_VALUE, null, PresentConstraintValue);
	}

	/**
	 * @return the presentConstraintConstraint
	 */
	public String getPresentConstraintConstraint() {
		return PresentConstraintConstraint;
	}

	/**
	 * @param presentConstraintConstraint the presentConstraintConstraint to set
	 */
	public void setPresentConstraintConstraint(String presentConstraintConstraint) {
		PresentConstraintConstraint = presentConstraintConstraint;
		firePropertyChange(P_PRESENTCONSTRAINT_CONSTRAINT, null, PresentConstraintConstraint);
	}

	/**
	 * @return the presentConstraintReset
	 */
	public String getPresentConstraintReset() {
		return PresentConstraintReset;
	}

	/**
	 * @param presentConstraintReset the presentConstraintReset to set
	 */
	public void setPresentConstraintReset(String presentConstraintReset) {
		PresentConstraintReset = presentConstraintReset;
		firePropertyChange(P_PRESENTCONSTRAINT_RESET, null, PresentConstraintReset);
	}

	
	
	public String getPastUnwantedMessageConstraintValue() {
		return PastUnwantedMessageConstraintValue;
	}

	
	public void setPastUnwantedMessageConstraintValue(
			String pastUnwantedMessageConstraintValue) {
		PastUnwantedMessageConstraintValue = pastUnwantedMessageConstraintValue;
		firePropertyChange(P_PASTUNWANTEDMESSAGECONSTRAINT_VALUE, null, PastUnwantedMessageConstraintValue);
	}

	
	public String getPastUnwantedMessageConstraintConstraint() {
		return PastUnwantedMessageConstraintConstraint;
	}

	
	public void setPastUnwantedMessageConstraintConstraint(
			String pastUnwantedMessageConstraintConstraint) {
		PastUnwantedMessageConstraintConstraint = pastUnwantedMessageConstraintConstraint;
		firePropertyChange(P_PASTUNWANTEDMESSAGECONSTRAINT_CONSTRAINT, null, PastUnwantedMessageConstraintConstraint);
	}

	
	public String getPastUnwantedMessageConstraintReset() {
		return PastUnwantedMessageConstraintReset;
	}

	
	public void setPastUnwantedMessageConstraintReset(
			String pastUnwantedMessageConstraintReset) {
		PastUnwantedMessageConstraintReset = pastUnwantedMessageConstraintReset;
		firePropertyChange(P_PASTUNWANTEDMESSAGECONSTRAINT_RESET, null, PastUnwantedMessageConstraintReset);
	}

	public String getFutureUnwantedMessageConstraintValue() {
		return FutureUnwantedMessageConstraintValue;
	}

	
	public void setFutureUnwantedMessageConstraintValue(
			String futureUnwantedMessageConstraintValue) {
		FutureUnwantedMessageConstraintValue = futureUnwantedMessageConstraintValue;
		firePropertyChange(P_FUTUREUNWANTEDMESSAGECONSTRAINT_VALUE, null, FutureUnwantedMessageConstraintValue);
	}

	
	public String getFutureUnwantedMessageConstraintConstraint() {
		return FutureUnwantedMessageConstraintConstraint;
	}

	
	public void setFutureUnwantedMessageConstraintConstraint(
			String futureUnwantedMessageConstraintConstraint) {
		FutureUnwantedMessageConstraintConstraint = futureUnwantedMessageConstraintConstraint;
		firePropertyChange(P_FUTUREUNWANTEDMESSAGECONSTRAINT_CONSTRAINT, null, FutureUnwantedMessageConstraintConstraint);
	}

	
	public String getFutureUnwantedMessageConstraintReset() {
		return FutureUnwantedMessageConstraintReset;
	}

	public void setFutureUnwantedMessageConstraintReset(
			String futureUnwantedMessageConstraintReset) {
		FutureUnwantedMessageConstraintReset = futureUnwantedMessageConstraintReset;
		firePropertyChange(P_FUTUREUNWANTEDMESSAGECONSTRAINT_RESET, null, FutureUnwantedMessageConstraintReset);
	}

	
	
	/*
	 
	public String getPastBooleanConstraintValue() {
		return PastBooleanConstraintValue;
	}

	
	public void setPastBooleanConstraintValue(
			String pastBooleanConstraintValue) {
		PastBooleanConstraintValue = pastBooleanConstraintValue;
		firePropertyChange(P_PASTBOOLEANCONSTRAINT_VALUE, null, PastBooleanConstraintValue);
	}

	
	public String getPastBooleanConstraintConstraint() {
		return PastBooleanConstraintConstraint;
	}

	
	public void setPastBooleanConstraintConstraint(
			String pastBooleanConstraintConstraint) {
		PastBooleanConstraintConstraint = pastBooleanConstraintConstraint;
		firePropertyChange(P_PASTBOOLEANCONSTRAINT_CONSTRAINT, null, PastBooleanConstraintConstraint);
	}

	
	public String getPastBooleanConstraintReset() {
		return PastBooleanConstraintReset;
	}

	
	public void setPastBooleanConstraintReset(
			String pastBooleanConstraintReset) {
		PastBooleanConstraintReset = pastBooleanConstraintReset;
		firePropertyChange(P_PASTBOOLEANCONSTRAINT_RESET, null, PastBooleanConstraintReset);
	}

	public String getFutureBooleanConstraintValue() {
		return FutureBooleanConstraintValue;
	}

	
	public void setFutureBooleanConstraintValue(
			String futureBooleanConstraintValue) {
		FutureBooleanConstraintValue = futureBooleanConstraintValue;
		firePropertyChange(P_FUTUREBOOLEANCONSTRAINT_VALUE, null, FutureBooleanConstraintValue);
	}

	
	public String getFutureBooleanConstraintConstraint() {
		return FutureBooleanConstraintConstraint;
	}

	
	public void setFutureBooleanConstraintConstraint(
			String futureBooleanConstraintConstraint) {
		FutureBooleanConstraintConstraint = futureBooleanConstraintConstraint;
		firePropertyChange(P_FUTUREBOOLEANCONSTRAINT_CONSTRAINT, null, FutureBooleanConstraintConstraint);
	}

	
	public String getFutureBooleanConstraintReset() {
		return FutureBooleanConstraintReset;
	}

	public void setFutureBooleanConstraintReset(
			String futureBooleanConstraintReset) {
		FutureBooleanConstraintReset = futureBooleanConstraintReset;
		firePropertyChange(P_FUTUREBOOLEANCONSTRAINT_RESET, null, FutureBooleanConstraintReset);
	}
	*/
	
	
	
	/**
	 * @return the presentPastValue
	 */
	public String getPresentPastValue() {
		return PresentPastValue;
	}

	/**
	 * @param presentPastValue the presentPastValue to set
	 */
	public void setPresentPastValue(String presentPastValue) {
		PresentPastValue = presentPastValue;
		firePropertyChange(P_PRESENTPAST_VALUE, null, PresentPastValue);
	}

	/**
	 * @return the presentPastConstraint
	 */
	public String getPresentPastConstraint() {
		return PresentPastConstraint;
	}

	/**
	 * @param presentPastConstraint the presentPastConstraint to set
	 */
	public void setPresentPastConstraint(
			String presentPastConstraint) {
		PresentPastConstraint = presentPastConstraint;
		firePropertyChange(P_PRESENTPAST_CONSTRAINT, null, PresentPastConstraint);
	}

	/**
	 * @return the presentPastReset
	 */
	public String getPresentPastReset() {
		return PresentPastReset;
	}

	/**
	 * @param presentPastReset the presentPastReset to set
	 */
	public void setPresentPastReset(String presentPastReset) {
		PresentPastReset = presentPastReset;
		firePropertyChange(P_PRESENTPAST_RESET, null, PresentPastReset);
	}
	
	/**
	 * @return the presentFutureValue
	 */
	public String getPresentFutureValue() {
		return PresentFutureValue;
	}

	/**
	 * @param presentFutureValue the presentFutureValue to set
	 */
	public void setPresentFutureValue(String presentFutureValue) {
		PresentFutureValue = presentFutureValue;
		firePropertyChange(P_PRESENTFUTURE_VALUE, null, PresentFutureValue);
	}

	/**
	 * @return the presentFutureConstraint
	 */
	public String getPresentFutureConstraint() {
		return PresentFutureConstraint;
	}

	/**
	 * @param presentFutureConstraint the presentFutureConstraint to set
	 */
	public void setPresentFutureConstraint(
			String presentFutureConstraint) {
		PresentFutureConstraint = presentFutureConstraint;
		firePropertyChange(P_PRESENTFUTURE_CONSTRAINT, null, PresentFutureConstraint);
	}

	/**
	 * @return the presentFutureReset
	 */
	public String getPresentFutureReset() {
		return PresentFutureReset;
	}

	/**
	 * @param presentFutureReset the presentFutureReset to set
	 */
	public void setPresentFutureReset(String presentFutureReset) {
		PresentFutureReset = presentFutureReset;
		firePropertyChange(P_PRESENTFUTURE_RESET, null, PresentFutureReset);
	}

	/**
	 * @return the PastWantedChainConstraintValue
	 */
	public String getPastWantedChainConstraintValue() {
		return PastWantedChainConstraintValue;
	}

	/**
	 * @param PastWantedChainConstraintValue the PastWantedChainConstraintValue to set
	 */
	public void setPastWantedChainConstraintValue(String openPastChainConstraintValue) {
		PastWantedChainConstraintValue = openPastChainConstraintValue;
		firePropertyChange(P_PASTWANTEDCHAINCONSTRAINT_VALUE, null, PastWantedChainConstraintValue);
	}

	/**
	 * @return the PastWantedChainConstraintConstraint
	 */
	public String getPastWantedChainConstraintConstraint() {
		return PastWantedChainConstraintConstraint;
	}

	/**
	 * @param PastWantedChainConstraintConstraint the PastWantedChainConstraintConstraint to set
	 */
	public void setPastWantedChainConstraintConstraint(
			String openPastChainConstraintConstraint) {
		PastWantedChainConstraintConstraint = openPastChainConstraintConstraint;
		firePropertyChange(P_PASTWANTEDCHAINCONSTRAINT_CONSTRAINT, null, PastWantedChainConstraintConstraint);
	}

	/**
	 * @return the PastWantedChainConstraintReset
	 */
	public String getPastWantedChainConstraintReset() {
		return PastWantedChainConstraintReset;
	}

	/**
	 * @param PastWantedChainConstraintReset the PastWantedChainConstraintReset to set
	 */
	public void setPastWantedChainConstraintReset(String openPastChainConstraintReset) {
		PastWantedChainConstraintReset = openPastChainConstraintReset;
		firePropertyChange(P_PASTWANTEDCHAINCONSTRAINT_RESET, null, PastWantedChainConstraintReset);
	}

	/**
	 * @return the futureWantedChainConstraintValue
	 */
	public String getFutureWantedChainConstraintValue() {
		return FutureWantedChainConstraintValue;
	}

	/**
	 * @param futureWantedChainConstraintValue the futureWantedChainConstraintValue to set
	 */
	public void setFutureWantedChainConstraintValue(
			String futureWantedChainConstraintValue) {
		FutureWantedChainConstraintValue = futureWantedChainConstraintValue;
		firePropertyChange(P_FUTUREWANTEDCHAINCONSTRAINT_VALUE, null, FutureWantedChainConstraintValue);
	}

	/**
	 * @return the futureWantedChainConstraintConstraint
	 */
	public String getFutureWantedChainConstraintConstraint() {
		return FutureWantedChainConstraintConstraint;
	}

	/**
	 * @param futureWantedChainConstraintConstraint the futureWantedChainConstraintConstraint to set
	 */
	public void setFutureWantedChainConstraintConstraint(
			String futureWantedChainConstraintConstraint) {
		FutureWantedChainConstraintConstraint = futureWantedChainConstraintConstraint;
		firePropertyChange(P_FUTUREWANTEDCHAINCONSTRAINT_CONSTRAINT, null, FutureWantedChainConstraintConstraint);
	}

	/**
	 * @return the futureWantedChainConstraintReset
	 */
	public String getFutureWantedChainConstraintReset() {
		return FutureWantedChainConstraintReset;
	}

	/**
	 * @param futureWantedChainConstraintReset the futureWantedChainConstraintReset to set
	 */
	public void setFutureWantedChainConstraintReset(
			String futureWantedChainConstraintReset) {
		FutureWantedChainConstraintReset = futureWantedChainConstraintReset;
		firePropertyChange(P_FUTUREWANTEDCHAINCONSTRAINT_RESET, null, FutureWantedChainConstraintReset);
	}

	/**
	 * @return the pastUnwantedChainConstraintValue
	 */
	public String getPastUnwantedChainConstraintValue() {
		return PastUnwantedChainConstraintValue;
	}

	/**
	 * @param pastUnwantedChainConstraintValue the pastUnwantedChainConstraintValue to set
	 */
	public void setPastUnwantedChainConstraintValue(String pastUnwantedChainConstraintValue) {
		PastUnwantedChainConstraintValue = pastUnwantedChainConstraintValue;
		firePropertyChange(P_PASTUNWANTEDCHAINCONSTRAINT_VALUE, null, PastUnwantedChainConstraintValue);
	}

	/**
	 * @return the pastUnwantedChainConstraintConstraint
	 */
	public String getPastUnwantedChainConstraintConstraint() {
		return PastUnwantedChainConstraintConstraint;
	}

	/**
	 * @param pastUnwantedChainConstraintConstraint the pastUnwantedChainConstraintConstraint to set
	 */
	public void setPastUnwantedChainConstraintConstraint(
			String pastUnwantedChainConstraintConstraint) {
		PastUnwantedChainConstraintConstraint = pastUnwantedChainConstraintConstraint;
		firePropertyChange(P_PASTUNWANTEDCHAINCONSTRAINT_CONSTRAINT, null, PastUnwantedChainConstraintConstraint);
	}

	/**
	 * @return the pastUnwantedChainConstraintReset
	 */
	public String getPastUnwantedChainConstraintReset() {
		return PastUnwantedChainConstraintReset;
	}

	/**
	 * @param pastUnwantedChainConstraintReset the pastUnwantedChainConstraintReset to set
	 */
	public void setPastUnwantedChainConstraintReset(String pastUnwantedChainConstraintReset) {
		PastUnwantedChainConstraintReset = pastUnwantedChainConstraintReset;
		firePropertyChange(P_PASTUNWANTEDCHAINCONSTRAINT_RESET, null, PastUnwantedChainConstraintReset);
	}

	/**
	 * @return the futureUnwantedChainConstraintValue
	 */
	public String getFutureUnwantedChainConstraintValue() {
		return FutureUnwantedChainConstraintValue;
	}

	/**
	 * @param futureUnwantedChainConstraintValue the futureUnwantedChainConstraintValue to set
	 */
	public void setFutureUnwantedChainConstraintValue(String futureUnwantedChainConstraintValue) {
		FutureUnwantedChainConstraintValue = futureUnwantedChainConstraintValue;
		firePropertyChange(P_FUTUREUNWANTEDCHAINCONSTRAINT_VALUE, null, FutureUnwantedChainConstraintValue);
	}

	/**
	 * @return the futureUnwantedChainConstraintConstraint
	 */
	public String getFutureUnwantedChainConstraintConstraint() {
		return FutureUnwantedChainConstraintConstraint;
	}

	/**
	 * @param futureUnwantedChainConstraintConstraint the futureUnwantedChainConstraintConstraint to set
	 */
	public void setFutureUnwantedChainConstraintConstraint(
			String futureUnwantedChainConstraintConstraint) {
		FutureUnwantedChainConstraintConstraint = futureUnwantedChainConstraintConstraint;
		firePropertyChange(P_FUTUREUNWANTEDCHAINCONSTRAINT_CONSTRAINT, null, FutureUnwantedChainConstraintConstraint);
	}

	/**
	 * @return the futureUnwantedChainConstraintReset
	 */
	public String getFutureUnwantedChainConstraintReset() {
		return FutureUnwantedChainConstraintReset;
	}

	/**
	 * @param futureUnwantedChainConstraintReset the futureUnwantedChainConstraintReset to set
	 */
	public void setFutureUnwantedChainConstraintReset(String futureUnwantedChainConstraintReset) {
		FutureUnwantedChainConstraintReset = futureUnwantedChainConstraintReset;
		firePropertyChange(P_FUTUREUNWANTEDCHAINCONSTRAINT_RESET, null, FutureUnwantedChainConstraintReset);
	}
	
	public Point getLocation() {
		return location;
	}

	/**
	 * @param location 
	 *         the location to set
	 */
	public void setLocation(Point newLocation) {
		//this.location = location;
		//firePropertyChange(P_LOCATION, null, this.location);
		if (newLocation == null) {
			throw new IllegalArgumentException();
		}
		this.location = newLocation;
		firePropertyChange(P_LOCATION, null, this.location);
	}
	
	
}