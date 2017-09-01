package org.springframework.security.oauth2.blockchain.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * The MIT License
 * Copyright (c) 2014-2017 Anubandhan Singh Sengar
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

@Component
public class SchedulerJobInitializer implements ApplicationListener<ContextRefreshedEvent>
{
    private static final Log logger = LogFactory.getLog(SchedulerJobInitializer.class);

    @Autowired
    boolean isInitialized = false;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event)
    {
        if(!isInitialized){
            isInitialized=true;
            logger.info("Trying to initialise Blockchain Thread");
          try{

              new BlockMintThread().initializeJob();
              logger.debug("Initializing Job BlockMintThread Finished");
                }
                catch(Exception e)
                {
                    logger.debug("Unable to schedule Job BlockMintThread Error:"+ e.getMessage());
                }
            }

    }
}

