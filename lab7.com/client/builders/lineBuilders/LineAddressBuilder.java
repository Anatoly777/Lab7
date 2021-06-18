package com.lab7.client.builders.lineBuilders;

import com.lab7.common.io.InputManager;
import com.lab7.common.io.OutputManager;
import com.lab7.common.lab.Address;

import java.io.IOException;

public class LineAddressBuilder extends LineBuilder<Address> {


    public LineAddressBuilder(InputManager inputManager, OutputManager outputManager) {
        super(inputManager, outputManager);
    }

    public Address build() throws IOException {

        do {

            outputManager.print("Введите почтовый индекс: ");
            inputManager.nextLine();
        }

        while (!Address.Params.zipCode.parse(inputManager.getString()));
        do {

            outputManager.print("Введите улицу: ");
            inputManager.nextLine();

        }
        while (!Address.Params.street.parse(inputManager.getString()));
        return new Address(Address.Params.street.get(), Address.Params.zipCode.get());


    }
}
