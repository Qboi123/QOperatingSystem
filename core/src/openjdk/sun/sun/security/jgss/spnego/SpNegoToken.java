/*
 * Copyright 2005 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

package sun.security.jgss.spnego;

import java.io.*;
import java.util.*;
import org.ietf.jgss.*;
import sun.security.util.*;
import sun.security.jgss.*;

/** 
 * Astract class for SPNEGO tokens.
 * Implementation is based on RFC 2478 
 * 
 * NegotiationToken ::= CHOICE {
 * 	negTokenInit  [0]	 NegTokenInit,
 *	negTokenTarg  [1]	 NegTokenTarg }
 *
 *
 * @author Seema Malkani
 * @version 1.8, 05/05/07
 * @since 1.6
 */

abstract class SpNegoToken extends GSSToken {

    static final int NEG_TOKEN_INIT_ID = 0x00;
    static final int NEG_TOKEN_TARG_ID = 0x01;

    static enum NegoResult {
	ACCEPT_COMPLETE,
	ACCEPT_INCOMPLETE,
	REJECT,
    };

    private int tokenType;

    // property
    static final boolean DEBUG = SpNegoContext.DEBUG;

    /**
     * The object identifier corresponding to the SPNEGO GSS-API
     * mechanism.
     */
    public static ObjectIdentifier OID;

    static {
	try {
            OID = new ObjectIdentifier(SpNegoMechFactory.
                                       GSS_SPNEGO_MECH_OID.toString());
	} catch (IOException ioe) {
	  // should not happen
	}
    }

    /**
     * Creates SPNEGO token of the specified type.
     */
    protected SpNegoToken(int tokenType) {
	this.tokenType = tokenType;
    }

    /**
     * Returns the individual encoded SPNEGO token
     *
     * @return the encoded token 
     * @exception GSSException
     */
    abstract byte[] encode() throws GSSException;

    /**
     * Returns the encoded SPNEGO token
     * Note: inserts the required CHOICE tags
     *
     * @return the encoded token
     * @exception GSSException
     */
    byte[] getEncoded() throws IOException, GSSException {

	// get the token encoded value
	DerOutputStream token = new DerOutputStream();
	token.write(encode());

	// now insert the CHOICE
	switch (tokenType) {
            case NEG_TOKEN_INIT_ID:
		// Insert CHOICE of Negotiation Token
		DerOutputStream initToken = new DerOutputStream();
		initToken.write(DerValue.createTag(DerValue.TAG_CONTEXT,
				true, (byte) NEG_TOKEN_INIT_ID), token);
		return initToken.toByteArray();

            case NEG_TOKEN_TARG_ID:
		// Insert CHOICE of Negotiation Token
		DerOutputStream targToken = new DerOutputStream();
		targToken.write(DerValue.createTag(DerValue.TAG_CONTEXT,
				true, (byte) NEG_TOKEN_TARG_ID), token);
		return targToken.toByteArray();
	    default:
		return token.toByteArray();
        }
    }

    /**
     * Returns the SPNEGO token type
     *
     * @return the token type
     */
    final int getType() {
	return tokenType;
    }

    /**
     * Returns a string representing the token type.
     *
     * @param tokenType the token type for which a string name is desired
     * @return the String name of this token type
     */
    static String getTokenName(int type) {
	switch (type) {
            case NEG_TOKEN_INIT_ID:
                return "SPNEGO NegTokenInit";
            case NEG_TOKEN_TARG_ID:
                return "SPNEGO NegTokenTarg";
	    default:
		return "SPNEGO Mechanism Token";
        }
    }

    /**
     * Returns the enumerated type of the Negotiation result.
     *
     * @param result the negotiated result represented by integer
     * @return the enumerated type of Negotiated result
     */
    static NegoResult getNegoResultType(int result) {
	switch (result) {
	case 0:
                return NegoResult.ACCEPT_COMPLETE;
	case 1:
                return NegoResult.ACCEPT_INCOMPLETE;
	case 2:
                return NegoResult.REJECT;
	default:
		// unknown - return optimistic result
                return NegoResult.ACCEPT_COMPLETE;
	}
    }

    /**
     * Returns a string representing the negotiation result.
     *
     * @param result the negotiated result
     * @return the String message of this negotiated result
     */
    static String getNegoResultString(int result) {
	switch (result) {
	case 0:
                return "Accept Complete";
	case 1:
                return "Accept InComplete";
	case 2:
                return "Reject";
	default:
                return ("Unknown Negotiated Result: " + result);
	}
    }
}