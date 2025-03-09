import { CloseOutlined } from "@ant-design/icons";
import { Button, DatePicker, Form, InputNumber } from "antd";
import dayjs, { Dayjs } from "dayjs";
import {
  ImportInvoiceDetailState,
  useImportInvoiceStore,
} from "../../store/import-invoice-store";
import { useShallow } from "zustand/react/shallow";
import { useEffect } from "react";

interface NewBatchFormProps {
  importInvoiceDetail: ImportInvoiceDetailState;
}

const NewBatchForm: React.FC<NewBatchFormProps> = ({ importInvoiceDetail }) => {
  const [form] = Form.useForm();
  const {
    initBatch,
    deleteBatch,
    updateBatchManufacturingDate,
    updateBatchQuantity,
  } = useImportInvoiceStore(
    useShallow((state) => ({
      updateBatchManufacturingDate: state.updateBatchManufacturingDate,
      updateBatchQuantity: state.updateBatchQuantity,
      initBatch: state.initBatch,
      deleteBatch: state.deleteBatch,
    })),
  );

  useEffect(() => {
    form.setFieldsValue({
      batches: importInvoiceDetail.batches,
    });
  }, [form, importInvoiceDetail.batches]);

  return (
    <Form form={form} layout="vertical">
      <Form.Item wrapperCol={{ span: 24 }}>
        <Form.List name="batches">
          {(batchFields, { add: addBatch, remove: removeBatch }) => {
            return (
              <>
                <div className="flex flex-col">
                  {batchFields.length > 0 && (
                    <div className="mb-2 flex items-center gap-10">
                      <div className="basis-[20%]">Ngày sản xuất</div>
                      <div className="basis-[20%]">Ngày hết hạn</div>
                      <div className="basis-[15%]">Số lượng</div>
                    </div>
                  )}
                  {batchFields.map((batchField) => {
                    return (
                      <div
                        className="flex items-center gap-10"
                        key={batchField.key}
                      >
                        <Form.Item
                          className="basis-[20%]"
                          name={[batchField.name, "manufacturingDate"]}
                          getValueProps={(value: string) => ({
                            value: value && dayjs(value),
                          })}
                          normalize={(value: Dayjs) =>
                            value && value.tz().format("YYYY-MM-DD")
                          }
                        >
                          <DatePicker
                            value={
                              importInvoiceDetail.batches &&
                              dayjs(
                                importInvoiceDetail.batches[batchField.name]
                                  .manufacturingDate,
                              )
                            }
                            disabledDate={(current) => {
                              return dayjs(current).isAfter(
                                dayjs().endOf("day"),
                              );
                            }}
                            onChange={(date: Dayjs) => {
                              form.setFieldValue(
                                ["batches", batchField.name, "expirationDate"],
                                date
                                  .add(
                                    importInvoiceDetail.product.defaultExpDays,
                                    "day",
                                  )
                                  .tz()
                                  .format("YYYY-MM-DD"),
                              );
                              updateBatchManufacturingDate(
                                importInvoiceDetail.product.productId,
                                batchField.name,
                                date.tz().format("YYYY-MM-DD"),
                              );
                            }}
                            className="w-full"
                            format="DD/MM/YYYY"
                          />
                        </Form.Item>
                        <Form.Item
                          className="basis-[20%]"
                          name={[batchField.name, "expirationDate"]}
                          getValueProps={(value: string) => ({
                            value: value && dayjs(value),
                          })}
                          normalize={(value: Dayjs) =>
                            value && value.tz().format("YYYY-MM-DD")
                          }
                        >
                          <DatePicker
                            value={
                              importInvoiceDetail.batches &&
                              dayjs(
                                importInvoiceDetail.batches[batchField.name]
                                  .expirationDate,
                              )
                            }
                            disabledDate={(current) => {
                              return dayjs(current).isBefore(
                                dayjs().startOf("day"),
                              );
                            }}
                            className="w-full"
                            format="DD/MM/YYYY"
                          />
                        </Form.Item>
                        <Form.Item
                          className="basis-[15%]"
                          name={[batchField.name, "quantity"]}
                          rules={[
                            {
                              validator: async () => {
                                const totalQuantity = batchFields
                                  .map((field) =>
                                    form.getFieldValue(["batches", field.name]),
                                  )
                                  .reduce(
                                    (acc, curr) => acc + curr.quantity,
                                    0,
                                  );
                                if (
                                  totalQuantity > importInvoiceDetail.quantity
                                ) {
                                  return Promise.reject();
                                }
                                return Promise.resolve();
                              },
                            },
                          ]}
                        >
                          <InputNumber
                            onChange={(value) => {
                              updateBatchQuantity(
                                importInvoiceDetail.product.productId,
                                batchField.name,
                                value as number,
                              );
                            }}
                            className="w-full"
                            min={1}
                          />
                        </Form.Item>
                        <Form.Item className="basis-[5%]">
                          <CloseOutlined
                            onClick={(
                              event: React.MouseEvent<
                                HTMLSpanElement,
                                MouseEvent
                              >,
                            ) => {
                              event.stopPropagation();
                              removeBatch(batchField.name);
                              deleteBatch(
                                importInvoiceDetail.product.productId,
                                batchField.name,
                              );
                            }}
                          />
                        </Form.Item>
                      </div>
                    );
                  })}

                  <Button
                    className="w-[150px]"
                    onClick={() => {
                      addBatch();
                      initBatch(importInvoiceDetail.product.productId);
                    }}
                  >
                    + Thêm lô hàng
                  </Button>
                </div>
              </>
            );
          }}
        </Form.List>
      </Form.Item>
    </Form>
  );
};

export default NewBatchForm;
