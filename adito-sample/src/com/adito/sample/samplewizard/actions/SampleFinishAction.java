
				/*
 *  Adito
 *
 *  Copyright (C) 2003-2006 3SP LTD. All Rights Reserved
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2 of
 *  the License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public
 *  License along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
			
package com.adito.sample.samplewizard.actions;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.adito.core.CoreEvent;
import com.adito.core.CoreServlet;
import com.adito.policyframework.ResourceChangeEvent;
import com.adito.sample.Sample;
import com.adito.sample.SamplePlugin;
import com.adito.sample.samplewizard.forms.SampleDefaultDetailsForm;
import com.adito.security.SessionInfo;
import com.adito.wizard.AbstractWizardSequence;
import com.adito.wizard.WizardActionStatus;
import com.adito.wizard.actions.AbstractWizardAction;
import com.adito.wizard.forms.AbstractWizardFinishForm;

/**
 * <p>
 * The final action in which the resource is created.
 * 
 * @author James D Robinson <a href="mailto:james@localhost">&lt;james@localhost&gt;</a>
 * 
 */
public class SampleFinishAction extends AbstractWizardAction {
    final static Log log = LogFactory.getLog(SampleFinishAction.class);

    /**
     * Constructor.
     */
    public SampleFinishAction() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.adito.core.actions.CoreAction#getNavigationContext(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public int getNavigationContext(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return SessionInfo.USER_CONSOLE_CONTEXT | SessionInfo.MANAGEMENT_CONSOLE_CONTEXT;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                    HttpServletResponse response) throws Exception {
        List actionStatus = new ArrayList();
        AbstractWizardSequence seq = getWizardSequence(request);
        String name = (String) seq.getAttribute(SampleDefaultDetailsForm.ATTR_RESOURCE_NAME, null);
        String description = (String) seq.getAttribute(SampleDefaultDetailsForm.ATTR_RESOURCE_DESCRIPTION, null);
        boolean favorite = ((Boolean) seq.getAttribute(SampleDefaultDetailsForm.ATTR_FAVORITE, Boolean.FALSE)).booleanValue();
        int parentResourcePermission = ((Integer) seq.getAttribute(SampleDefaultDetailsForm.ATTR_PARENT_RESOURCE_PERMISSION,
                        new Integer(0))).intValue();
        Sample sample = null;
        try {
            try {
                sample = SamplePlugin.getDatabase().addSample(name, description, parentResourcePermission);
                // #ifdef XTRA
                CoreServlet.getServlet().fireCoreEvent(new ResourceChangeEvent(this, 
                    SamplePlugin.EVT_SAMPLE_CREATED, sample, getSessionInfo(request), CoreEvent.STATE_SUCCESSFUL));
                // #endif
            }
            catch(Exception e) {
                // #ifdef XTRA
                CoreServlet.getServlet().fireCoreEvent(new ResourceChangeEvent(this, 
                    SamplePlugin.EVT_SAMPLE_CREATED, sample, getSessionInfo(request), CoreEvent.STATE_UNSUCCESSFUL));
                // #endif
                throw e;
            }        
            actionStatus.add(new WizardActionStatus(WizardActionStatus.COMPLETED_OK,
                            "samplewizard.sampleFinish.status.profileCreated"));
        } catch (Exception e) {
            log.error("Failed to create profile.", e);
            actionStatus.add(new WizardActionStatus(WizardActionStatus.COMPLETED_WITH_ERRORS,
                            "samplewizard.sampleFinish.status.failedToCreateProfile", e.getMessage()));
        }
        // TODO do the attaching.
        if (sample != null) {
            actionStatus.add(attachToPoliciesAndAddToFavorites("samplewizard.sampleFinish", seq, sample, favorite, request));
        }
        ((AbstractWizardFinishForm) form).setActionStatus(actionStatus);
        return super.unspecified(mapping, form, request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.adito.wizard.actions.AbstractFinishWizardAction#exit(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ActionForward exit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
                    throws Exception {
        return cancel(mapping, form, request, response);
    }

}
