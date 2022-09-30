import {
  Space,
  Table,
  Tag,
  Button,
  Col,
  Form,
  Input,
  Row,
  Select,
  UpOutlined,
  DownOutlined,
  message,
  Popconfirm,
  Modal,
  TextArea,
  Radio,
  Upload,
  PlusOutlined,
} from 'antd';
import React, { useState, useEffect } from 'react';
import fomatterApi from '@/services/spark/fomatterApi';

const FomatterCreateModal = (props) => {
  const [confirmLoading, setConfirmLoading] = useState(false);

  const [allFormatterType, setAllFormatterType] = useState([]);

  const [form] = Form.useForm();

  const handleOk = () => {
    form.submit();
  };

  const handleCancel = () => {
    props.closeCreateModal();
  };

  useEffect(() => {
    fomatterApi.getAllFormatterType().then((resp) => {
      if (resp.success) {
        setAllFormatterType(resp.data);
      }
    });
  }, []);

  const doCreateNew = (values) => {
    setConfirmLoading(true);
    fomatterApi.createNew({ ...values }).then((resp) => {
      if (resp.success) {
        message.success('operate successfully!');
        props.closeCreateModal(true);
      }
      setConfirmLoading(false);
    });
  };

  return (
    <>
      <Modal
        title="Create New Formatter"
        //open={open}
        visible={true}
        onOk={handleOk}
        confirmLoading={confirmLoading}
        onCancel={handleCancel}
      >
        <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
          <Form
            form={form}
            name="advanced_search"
            className="ant-advanced-search-form"
            layout="vertical"
            onFinish={doCreateNew}
            labelAlign="right"
          >
            <Form.Item
              name="name"
              label="Name (unique)"
              rules={[{ required: true, message: 'Missing  Name' }]}
            >
              <Input style={{ width: 400 }} />
            </Form.Item>

            <Form.Item
              name="formatterType"
              label="Formatter Type"
              rules={[{ required: true, message: 'Missing Outbound Type' }]}
            >
              <Select
                options={[            
                  ...allFormatterType,
                ]}
                style={{ width: 400 }}
                onChange={(value) => {}}
              />
            </Form.Item>

            <Form.Item
              name="template"
              label="Template ( Complex templates will be supported in the future )"
            >
              <Input.TextArea
                style={{ width: 400, height: 200 }}
                showCount
                placeholder="input  template text "
              />
              {/* <div style={{ fontSize: 15, marginTop: 10 }}> OR </div> */}
            </Form.Item>

            <Form.Item label="Upload Template" valuePropName="fileList">
              <Upload action="/api/formatter/upload" listType="picture-card">
                <div>
                  {/* <PlusOutlined /> */}
                  <div
                    style={{
                      marginTop: 8,
                    }}
                  >
                    Upload
                  </div>
                </div>
              </Upload>
            </Form.Item>
          </Form>
        </div>
      </Modal>
    </>
  );
};

export default FomatterCreateModal;
