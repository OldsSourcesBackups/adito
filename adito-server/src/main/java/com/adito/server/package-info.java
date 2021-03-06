/* <p>Contains class for the default container implementation in which the Adito
 * web applications runs.</p>
 * <p>Adito is primarily a standard Java web application. However, it requires a
 * few additional services from the container that it is running in. 
 * This environment is called the {@link com.adito.boot.Context} 
 * (see this interfaces Javadoc for more information about this environment).</p>
 * <p>The current implementation of this Context is made up of classes contained 
 * in this package and makes use of :-
 * <ul>
 * <li>Jetty - As the servlet / JSP container required for struts and Adito itself.</li>
 * <li>HSQLDB - For the Database engine</li>
 * <li>Wrapper - For service control and JVM resilience.</li>
 * </ul></p>
 * <p>Although this is the only implementation, it is envisaged that there may one
 * day be a requirement for Adito to run on other platforms made up of components
 * other than those above. For example, in theory it should be possible for Adito
 * to run using Tomcat for the servlet container and MySQL for the database.</p>
 * <p>In practice thought, there are still a few areas when Jetty code is used inside
 * the webapp itself, so a little more refactoring will be necessary.</p>
 */
package com.adito.server;
