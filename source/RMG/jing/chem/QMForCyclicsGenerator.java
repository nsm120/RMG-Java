package jing.chem;

import jing.rxnSys.Logger;

public class QMForCyclicsGenerator extends TDGenerator {

	public QMForCyclicsGenerator(){
		thermoGAPP = GATP.getINSTANCE();
		thermoQM = QMTP.getINSTANCE();
	}

	/**
	 * Generates thermochemistry for acyclic species based on Benson.
	 * Tries to estimate thermo for cyclic species based on the QM methods.
	 * If multiple QM attempts fail, fall back to the Benson approach.
	 */
	@Override
	public ThermoData generateThermo(ChemGraph chemGraph) {
		if(chemGraph.isAcyclic()){
			return thermoGAPP.generateThermoData(chemGraph);
		}
		else{
			ThermoData thermo = thermoQM.generateThermoData(chemGraph);
			
			if (((String)thermo.getSource()).equals("***failed calculation***")){
			    Logger.warning("Falling back to group additivity due to repeated failure in QMTP calculations");
			    TDGenerator gen = new BensonTDGenerator();
    			return gen.generateThermo(chemGraph);
			}
			
			return thermo;
		}
	}

}
