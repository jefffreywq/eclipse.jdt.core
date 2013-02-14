/*******************************************************************************
 * Copyright (c) 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * This is an implementation of an early-draft specification developed under the Java
 * Community Process (JCP) and is made available for testing and evaluation purposes
 * only. The code is not compatible with any specification of the JCP.
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.lookup;

import org.eclipse.jdt.internal.compiler.ast.Wildcard;
import org.eclipse.jdt.internal.compiler.ast.Annotation.TypeUseBinding;


public class TypeBindingVisitor {

	public boolean visit(BaseTypeBinding baseTypeBinding)  {
		return true;  // continue traversal.
	}

	public boolean visit(ArrayBinding arrayBinding) {
		return true;  // continue traversal.
	}
	
	public boolean visit(TypeVariableBinding typeVariable) {
		return true;  // continue traversal.
	}
	
	public boolean visit(ReferenceBinding referenceBinding) {
		return true;  // continue traversal.
	}
	
	public boolean visit(WildcardBinding wildcardBinding) {
		return true;  // continue traversal.
	}
	
	public boolean visit(ParameterizedTypeBinding parameterizedTypeBinding) {
		return true;  // continue traversal.
	}
	
	public boolean visit(IntersectionCastTypeBinding intersectionCastTypeBinding) {
		return true;  // continue traversal.
	}
	
	public boolean visit(TypeUseBinding typeUseBinding) {
		return true;  // continue traversal.
	}
	
	public boolean visit(RawTypeBinding rawTypeBinding) {
		return true;  // continue traversal.
	}

	public static void visit(TypeBindingVisitor visitor, ReferenceBinding[] types) {
		for (int i = 0, length = types == null ? 0 : types.length; i < length; i++) {
	        visit(visitor, types[i]);
	    }
	}

	public static void visit(TypeBindingVisitor visitor, TypeBinding type) {

		if (type == null) 
			return;
		
		switch (type.kind()) {
			
			case Binding.TYPE_PARAMETER:
				visitor.visit((TypeVariableBinding) type);
	            break;
	            
			case Binding.PARAMETERIZED_TYPE:
				ParameterizedTypeBinding parameterizedTypeBinding = (ParameterizedTypeBinding) type;
				if (visitor.visit(parameterizedTypeBinding)) {
					visit(visitor, parameterizedTypeBinding.enclosingType());
					visit(visitor, parameterizedTypeBinding.arguments);
				}
				break;

			case Binding.ARRAY_TYPE:
				ArrayBinding arrayBinding = (ArrayBinding) type;
				if (visitor.visit(arrayBinding))
					visit(visitor, arrayBinding.leafComponentType);
				break;

			case Binding.WILDCARD_TYPE:
			case Binding.INTERSECTION_TYPE:
		        WildcardBinding wildcard = (WildcardBinding) type;
		        if (visitor.visit(wildcard)) {
		        	if (wildcard.boundKind != Wildcard.UNBOUND) {
		        		visit(visitor, wildcard.bound);
		        		visit(visitor, wildcard.otherBounds);
		        	}
		        }
				break;
			
			case Binding.BASE_TYPE:
				visitor.visit((BaseTypeBinding) type);
				break;
			
			case Binding.RAW_TYPE:
				visitor.visit((RawTypeBinding) type);
				break;
				
			case Binding.TYPE:
			case Binding.GENERIC_TYPE:
				ReferenceBinding referenceBinding = (ReferenceBinding) type;
				if (visitor.visit(referenceBinding)) {
					visit(visitor, referenceBinding.enclosingType());
					visit(visitor, referenceBinding.typeVariables());
				}
				break;
			
			case Binding.INTERSECTION_CAST_TYPE:
				IntersectionCastTypeBinding intersectionCastTypeBinding = (IntersectionCastTypeBinding) type;
				if (visitor.visit(intersectionCastTypeBinding))
					visit(visitor, intersectionCastTypeBinding.intersectingTypes);
				break;
				
			case Binding.TYPE_USE:
				visitor.visit((TypeUseBinding) type);
				break;
				
			default:
				throw new InternalError("Unexpected binding type"); //$NON-NLS-1$
		}
	}

	public static void visit(TypeBindingVisitor visitor, TypeBinding[] types) {
		for (int i = 0, length = types == null ? 0 : types.length; i < length; i++) {
	        visit(visitor, types[i]);
	    }
	}
}