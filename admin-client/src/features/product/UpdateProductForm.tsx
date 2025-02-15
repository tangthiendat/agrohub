import { CloseOutlined, PlusOutlined } from "@ant-design/icons";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import toast from "react-hot-toast";
import {
  Alert,
  Button,
  Form,
  Image,
  Input,
  InputNumber,
  Modal,
  Select,
  Space,
  Switch,
  Typography,
  Upload,
  UploadFile,
  UploadProps,
} from "antd";
import { useEffect, useState } from "react";
import EditProductUnitPrice from "./EditProductUnitPrice";
import { ProductUnitPriceContextProvider } from "../../context/ProductUnitPriceContext";
import { PHYSICAL_STATE_NAME } from "../../common/constants/product";
import { useTitle } from "../../common/hooks";
import {
  ApiResponse,
  FileType,
  ICategory,
  IProduct,
  IProductUnit,
  IUnit,
} from "../../interfaces";
import { categoryService, productService, unitService } from "../../services";
import { getBase64 } from "../../utils/image";

interface UpdateProductFormProps {
  productToUpdate?: IProduct;
  onCancel: () => void;
  viewOnly?: boolean;
}

const physicalStateOptions = Object.entries(PHYSICAL_STATE_NAME).map(
  ([key, value]) => ({
    value: key,
    label: value,
  }),
);

const UpdateProductForm: React.FC<UpdateProductFormProps> = ({
  productToUpdate,
  onCancel,
  viewOnly = false,
}) => {
  useTitle("Thêm sản phẩm");
  const queryClient = useQueryClient();
  const [form] = Form.useForm<IProduct>();
  const [fileList, setFileList] = useState<UploadFile[]>([]);
  const [previewOpen, setPreviewOpen] = useState<boolean>(false);
  const [previewImage, setPreviewImage] = useState<string>("");
  const [modal, contextHolder] = Modal.useModal();

  useEffect(() => {
    if (productToUpdate) {
      form.setFieldsValue(productToUpdate);
      if (productToUpdate.imageUrl) {
        setPreviewImage(productToUpdate.imageUrl);
        setFileList([
          {
            uid: "-1",
            name: "image.png",
            status: "done",
            url: productToUpdate.imageUrl,
          },
        ]);
      }
    }
  }, [productToUpdate, form]);

  const { data: categoryOptions, isLoading: isCategoriesLoading } = useQuery({
    queryKey: ["categories"],
    queryFn: categoryService.getAll,
    select: (data: ApiResponse<ICategory[]>) => {
      if (data.payload) {
        return data.payload.map((category) => ({
          value: category.categoryId,
          label: category.categoryName,
        }));
      }
    },
  });

  const { data: unitOptions, isLoading: isUnitsLoading } = useQuery({
    queryKey: ["unit"],
    queryFn: unitService.getAll,
    select: (data: ApiResponse<IUnit[]>) => {
      if (data.payload) {
        return data.payload.map((unit) => ({
          value: unit.unitId,
          label: unit.unitName,
        }));
      }
    },
  });

  const { mutate: createProduct, isPending: isCreating } = useMutation({
    mutationFn: productService.create,
    onSuccess: () => {
      queryClient.invalidateQueries({
        predicate: (query) => {
          return query.queryKey.includes("products");
        },
      });
    },
  });

  async function handlePreview(file: UploadFile) {
    if (!file.url && !file.preview) {
      file.preview = await getBase64(file.originFileObj as FileType);
    }
    setPreviewImage(file.url || file.preview || "");
    setPreviewOpen(true);
  }

  const handleUploadChange: UploadProps["onChange"] = ({ fileList }) => {
    setFileList(fileList);
  };

  function handleIsDefaultChange(
    isDefault: boolean,
    formListItemIndex: number,
  ): void {
    const productUnits: IProductUnit[] = form.getFieldValue("productUnits");
    const updatedProductUnits: IProductUnit[] = productUnits.map(
      (productUnit, index) => {
        if (index === formListItemIndex) {
          return {
            ...productUnit,
            isDefault,
          };
        }
        return { ...productUnit, isDefault: false };
      },
    );
    form.setFieldsValue({
      productUnits: updatedProductUnits,
    });
  }

  function handleFinish() {
    if (productToUpdate) {
      console.log("update product");
    } else {
      const productValues = form.getFieldsValue();
      const hasProductUnit: boolean = productValues.productUnits?.length > 0;
      //Check whether the product has a default unit
      if (hasProductUnit) {
        const hasBlankField: boolean = productValues.productUnits.some(
          (productUnit) =>
            !productUnit.unit?.unitId ||
            !productUnit.conversionFactor ||
            !productUnit.productUnitPrices ||
            productUnit.productUnitPrices.length === 0,
        );
        if (hasBlankField) {
          modal.error({
            title: "Thiếu thông tin",
            content: "Hãy điền đầy đủ thông tin cho các đơn vị tính.",
            centered: true,
          });
          return;
        }
        const hasDefaultUnit: boolean = productValues.productUnits.some(
          (productUnit) => productUnit.isDefault,
        );
        if (!hasDefaultUnit) {
          modal.error({
            title: "Thiếu thông tin",
            content: "Hãy chọn một đơn vị tính mặc định cho sản phẩm.",
            centered: true,
          });
          return;
        }
      } else {
        modal.error({
          title: "Thiếu thông tin",
          content: "Hãy thêm ít nhất một đơn vị tính cho sản phẩm.",
          centered: true,
        });
        return;
      }

      const formData = new FormData();
      formData.append("product", JSON.stringify(productValues));
      if (fileList.length > 0) {
        formData.append("productImg", fileList[0].originFileObj as File);
      }

      createProduct(formData, {
        onSuccess: () => {
          toast.success("Thêm sản phẩm thành công");
          onCancel();
          form.resetFields();
        },
        onError: () => {
          toast.error("Thêm sản phẩm thất bại");
        },
      });
    }
  }

  return (
    <>
      {contextHolder}
      <Form onFinish={handleFinish} layout="vertical" form={form}>
        <Typography.Title level={5} className="mb-2">
          Thông tin cơ bản
        </Typography.Title>

        <div className="flex items-center justify-between gap-10">
          <div className="flex-1">
            <Form.Item
              label="Tên sản phẩm"
              name="productName"
              rules={[
                {
                  required: true,
                  message: "Vui lòng nhập tên sản phẩm",
                },
              ]}
            >
              <Input readOnly={viewOnly} />
            </Form.Item>
          </div>

          <div className="flex-1">
            <Form.Item
              label="Loại sản phẩm"
              name={["category", "categoryId"]}
              rules={[
                {
                  required: true,
                  message: "Vui lòng chọn loại sản phẩm",
                },
              ]}
            >
              <Select
                disabled={viewOnly}
                options={categoryOptions}
                loading={isCategoriesLoading}
                placeholder="Chọn loại sản phẩm"
              />
            </Form.Item>
          </div>
        </div>

        <div className="flex items-center justify-between gap-10">
          <div className="flex-1">
            <Form.Item label="Mô tả" name="description">
              <Input.TextArea readOnly={viewOnly} rows={4} />
            </Form.Item>
          </div>
          <div className="flex-1">
            <Form.Item
              name="productImg"
              label="Ảnh sản phẩm"
              valuePropName="fileList"
              rules={[
                {
                  validator: () => {
                    if (fileList && fileList.length < 1) {
                      return Promise.reject(new Error("Vui lòng tải ảnh lên"));
                    }
                    return Promise.resolve();
                  },
                },
              ]}
              getValueFromEvent={(e) =>
                Array.isArray(e) ? e : e && e.fileList
              }
            >
              <Upload
                maxCount={1}
                disabled={viewOnly}
                listType="picture-card"
                fileList={fileList}
                beforeUpload={() => false} // Prevent automatic upload
                onPreview={handlePreview}
                onChange={handleUploadChange}
                showUploadList={{
                  showRemoveIcon: !viewOnly,
                }}
              >
                {fileList.length < 1 && (
                  <button
                    style={{ border: 0, background: "none" }}
                    type="button"
                  >
                    <PlusOutlined />
                    <div style={{ marginTop: 8 }}>Tải ảnh lên</div>
                  </button>
                )}
              </Upload>
              {previewImage && (
                <Image
                  wrapperStyle={{ display: "none" }}
                  preview={{
                    visible: previewOpen,
                    onVisibleChange: (visible) => setPreviewOpen(visible),
                    afterOpenChange: (visible) =>
                      !visible && setPreviewImage(""),
                  }}
                  src={previewImage}
                />
              )}
            </Form.Item>
          </div>
        </div>

        <div className="flex items-center justify-between gap-10">
          <div style={{ width: "calc(50% - 1.25rem)" }}>
            <Form.Item label="Tổng số lượng sản phẩm" name="totalQuantity">
              <InputNumber readOnly={viewOnly} className="w-full" min={0} />
            </Form.Item>
          </div>
        </div>

        <Typography.Title level={5} className="mb-2">
          Đặc điểm kỹ thuật
        </Typography.Title>

        <div className="flex items-center justify-between gap-10">
          <div className="flex-1">
            <Form.Item
              label="Thời hạn sử dụng (ngày)"
              name="defaultExpDays"
              tooltip="Thời gian sử dụng mặc định của sản phẩm trước khi hết hạn (ngày)"
              rules={[
                {
                  required: true,
                  message: "Vui lòng nhập thời hạn sử dụng",
                },
              ]}
            >
              <InputNumber readOnly={viewOnly} className="w-full" min={0} />
            </Form.Item>
          </div>

          <div className="flex-1">
            <Form.Item label="Điều kiện bảo quản" name="storageConditions">
              <Input readOnly={viewOnly} />
            </Form.Item>
          </div>
        </div>

        <div className="flex items-center justify-between gap-10">
          <div className="flex-1">
            <Form.Item
              label="Trạng thái vật lý"
              name="physicalState"
              rules={[
                {
                  required: true,
                  message: "Vui lòng chọn trạng thái vật lý",
                },
              ]}
            >
              <Select
                disabled={viewOnly}
                options={physicalStateOptions}
                placeholder="Chọn trạng thái vật lý"
              />
            </Form.Item>
          </div>

          <div className="flex-1">
            <Form.Item label="Quy cách đóng gói" name="packaging">
              <Input
                readOnly={viewOnly}
                placeholder="VD: 1kg, bao 50kg, chai 1L"
              />
            </Form.Item>
          </div>
        </div>

        <Typography.Title level={5} className="mb-2">
          An toàn và bảo vệ sức khỏe
        </Typography.Title>

        <div className="flex items-center justify-between gap-10">
          <div className="flex-1">
            <Form.Item label="Hướng dẫn an toàn" name="safetyInstructions">
              <Input readOnly={viewOnly} />
            </Form.Item>
          </div>

          <div className="flex-1">
            <Form.Item label="Phân loại nguy hiểm" name="hazardClassification">
              <Input
                readOnly={viewOnly}
                placeholder="VD: Độc hại, Gây kích ứng da"
              />
            </Form.Item>
          </div>
        </div>

        <div className="flex items-center justify-between gap-10">
          <div style={{ width: "calc(50% - 1.25rem)" }}>
            <Form.Item label="Thiết bị bảo hộ cần thiết" name="ppeRequired">
              <Input
                readOnly={viewOnly}
                placeholder="VD: Găng tay, kính bảo hộ"
              />
            </Form.Item>
          </div>
        </div>

        <Typography.Title level={5} className="mb-2">
          Đơn vị tính
        </Typography.Title>

        <Alert
          className="mb-4"
          type="warning"
          message="Chỉ có thể chọn một đơn vị tính là mặc định. Không thể chọn lại đơn vị tính mặc định sau khi tạo sản phẩm."
          showIcon
        />

        <Form.Item wrapperCol={{ span: 24 }}>
          <Form.List name="productUnits">
            {(unitFields, { add: addUnit, remove: removeUnit }) => {
              return (
                <>
                  <div className="flex flex-col">
                    {unitFields.length > 0 && (
                      <div className="mb-2 flex items-center justify-between font-semibold">
                        <div className="basis-[15%]">Đơn vị</div>
                        <div className="basis-[15%]">Hệ số quy đổi</div>
                        <div className="basis-[20%]">Giá</div>
                        <div className="basis-[15%]">Mặc định</div>
                        <div className="basis-[5%]"></div>
                      </div>
                    )}
                    {unitFields.map((unitField) => {
                      return (
                        <div
                          className="flex items-center justify-between"
                          key={unitField.key}
                        >
                          <Form.Item
                            className="basis-[15%]"
                            name={[unitField.name, "unit", "unitId"]}
                          >
                            <Select
                              disabled={viewOnly}
                              options={unitOptions}
                              loading={isUnitsLoading}
                            />
                          </Form.Item>
                          <Form.Item
                            className="basis-[15%]"
                            name={[unitField.name, "conversionFactor"]}
                          >
                            <InputNumber
                              readOnly={viewOnly}
                              className="w-full"
                              min={0}
                              max={1000000}
                            />
                          </Form.Item>

                          <Form.Item className="basis-[20%]">
                            <ProductUnitPriceContextProvider>
                              <EditProductUnitPrice
                                key={unitField.name}
                                productForm={form}
                                productUnitIndex={unitField.name}
                                viewOnly={viewOnly}
                              />
                            </ProductUnitPriceContextProvider>
                          </Form.Item>

                          <Form.Item
                            className="basis-[15%]"
                            name={[unitField.name, "isDefault"]}
                            valuePropName="checked"
                          >
                            <Switch
                              disabled={viewOnly}
                              onChange={(checked: boolean) =>
                                handleIsDefaultChange(checked, unitField.name)
                              }
                            />
                          </Form.Item>

                          <Form.Item className="basis-[5%]">
                            {!viewOnly && (
                              <CloseOutlined
                                onClick={() => removeUnit(unitField.name)}
                              />
                            )}
                          </Form.Item>
                        </div>
                      );
                    })}

                    {!viewOnly && (
                      <Button className="w-[150px]" onClick={() => addUnit()}>
                        + Thêm đơn vị tính
                      </Button>
                    )}
                  </div>
                </>
              );
            }}
          </Form.List>
        </Form.Item>

        {!viewOnly && (
          <Form.Item className="text-right" wrapperCol={{ span: 24 }}>
            <Space>
              <Button onClick={onCancel} loading={isCreating}>
                Hủy
              </Button>
              <Button type="primary" htmlType="submit" loading={isCreating}>
                {productToUpdate ? "Cập nhật" : "Thêm mới"}
              </Button>
            </Space>
          </Form.Item>
        )}
      </Form>
    </>
  );
};

export default UpdateProductForm;
