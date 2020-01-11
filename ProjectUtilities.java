
public class ProjectUtilities {

    public static boolean isNumeric(String inputData) {
        return inputData.matches("\\d+(\\d+)?");
    }

    public static final String documentTypes[] = { "Cédula de ciudadanía", "Tarjeta de identidad", "Cédula de extranjería", "Pasaporte"};
    public static final String clientTypes[] = { "Natural", "Corporativo"};

    public static int convertDocumentType(String  documentType){
        if(documentTypes[0].equals(documentType)){
            return 0;
        } else if (documentTypes[1].equals(documentType)){
            return 1;
        } else if (documentTypes[2].equals(documentType)){
            return 2;
        } else if (documentTypes[3].equals(documentType)){
            return 3;
        }
        return 0;
    }

    public static int convertClientType(String type){
        if (clientTypes[0].equals(type)){
            return 0;
        } else if (clientTypes[1].equals(type)){
            return 1;
        }
        return 0;
    }
}
