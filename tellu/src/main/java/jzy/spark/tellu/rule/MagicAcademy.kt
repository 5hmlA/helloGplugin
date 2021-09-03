package jzy.spark.tellu.rule

import jzy.spark.tellu.wizard.*

interface MagicAcademy {

    val dataWizard: DataWizard

    val detectWizard: DetectWizardOrganization

    val resetWizard: ResetWizard

    val perceptWizard: PerceptWizard

    val illusionWizard: IllusionWizardOrganization

}

/**
 * 霍格沃茨魔法学校
 */
class Hogwarts : MagicAcademy {

    override val dataWizard: DataWizard by lazy {
        DataWizardImpl()
    }

    override val detectWizard by lazy { DetectWizardOrganizationImpl() }

    override val resetWizard by lazy { ResetWizardImpl() }

    override val perceptWizard by lazy { PerceptWizardImpl() }

    override val illusionWizard by lazy { IllusionWizardOrganizationImpl() }

}

