package org.openflexo.ta.opcua.model;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.openflexo.pamela.exceptions.MissingImplementationException;
import org.openflexo.pamela.exceptions.ModelDefinitionException;

public class TestOPCUAModelFactory {

	@Test
	public void test() throws ModelDefinitionException {
		OPCModelFactory factory = new OPCModelFactory(null, null);
		try {
			factory.checkMethodImplementations();
		} catch (MissingImplementationException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
